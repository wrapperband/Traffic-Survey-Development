'''
Created on 18/01/2014

@author: dels
'''
    
ID = 0

if __name__ == '__main__':

    # ============== objects for the cars traveling UP or DOWN
    def carUp():
        pass
    carUp.day = 0
    carUp.startTime = 0
    
    def carDn():
        pass
    carDn.day = 0
    carDn.startTime = 0
    

    # =========================  Functions to create output records  ======================
    def nextId():
        nextId.counter += 1
        return nextId.counter
    nextId.counter = 0
    
    f = open('test-results.csv','w')
    
    def CreateRecord( direction, day, starttime, endtime ):
        ID = nextId()
        if ID == 1: 
            print >>f, "ID,Direction,Day,Time,Duration,Speed"
        duration = 1.0 * (ms_in_day + endtime - starttime) % ms_in_day
        speed =  2.5/1000/(duration/3600000)
        print >>f, "%5d,%3s,%4d,%9d,%5d,%5.1f" % (ID, direction, day, starttime, duration, speed)

    # =========================  functions to implement Finite State Machine
    def IDLE(source, timestamp, DeltaT, NewState):
        if source == "A":
            carDn.StartTime = timestamp; carDn.day = Day; NewState = DOWN_1
        return NewState
    RESYNC = IDLE
    
    def DOWN_1(source, timestamp, DeltaT, NewState):
        if source == "A":
            if DeltaT < DT1:
                carUp.StartTime = timestamp; carUp.day = Day; NewState = CC_1
            elif DeltaT < DT2:
                carUp.StartTime = timestamp; carUp.day = Day; NewState = OL2_1
            elif DeltaT < DT3:
                CreateRecord("DN", carDn.day, carDn.StartTime, timestamp)
                NewState = IDLE
        elif source == "B" and DeltaT < DT1:
            carUp.StartTime = timestamp; carUp.day = Day; NewState = UP_1
        return NewState

    def DOWN_2(source, timestamp, DeltaT, NewState):
        if source == "A":
            CreateRecord("DN", carDn.day, carDn.StartTime, timestamp); NewState = IDLE
        return NewState

    def UP_1(source, timestamp, DeltaT, NewState):
        if source == "A":
            if DeltaT < DT1:
                NewState = CC_2
            elif DeltaT < DT2:
                NewState = OL1_1
            elif DeltaT < DT3:
                NewState = UP_2
        return NewState

    def UP_2(source, timestamp, DeltaT, NewState):
        if source == "B" and DeltaT < DT1:
            CreateRecord("UP", carUp.day, carUp.StartTime, timestamp); NewState = IDLE
        return NewState

    def OL1_1(source, timestamp, DeltaT, NewState):
        if source == "A":
            NewState = OL1_2
        return NewState

    def OL1_2(source, timestamp, DeltaT, NewState):
        if source == "B" and DeltaT < DT1:
            CreateRecord("UP", carUp.day, carUp.StartTime, timestamp); NewState = DOWN_2
        return NewState

    def OL2_1(source, timestamp, DeltaT, NewState):
        if source == "B" and DeltaT < DT1:
            NewState = OL2_2
        return NewState

    def OL2_2(source, timestamp, DeltaT, NewState):
        if source == "A" and DeltaT < DT1:
            if DeltaT < DT2:
                CreateRecord("DN", carDn.day, carDn.StartTime, timestamp); NewState = OL2_3A
            elif DeltaT < DT3:
                NewState = OL2_3B
        return NewState
                
    def OL2_3A(source, timestamp, DeltaT, NewState):
        if source == "A" and DeltaT >= DT1 and DeltaT < DT2:
            NewState = UP_2
        return NewState

    def OL2_3B(source, timestamp, DeltaT, NewState):
        if source == "A" and DeltaT < DT1:
            CreateRecord("DN", carDn.day, carDn.StartTime, timestamp); NewState = UP_2
        elif source == "B" and DeltaT < DT1:
            CreateRecord("UP", carUp.day, carUp.StartTime, timestamp); NewState = OL2_5
        return NewState
                
    def OL2_5(source, timestamp, DeltaT, NewState):
        if source == "A" and DeltaT < DT1:
            CreateRecord("DN", carDn.day, carDn.StartTime, timestamp); NewState = IDLE
        return NewState

    def CC_1(source, timestamp, DeltaT, NewState):
        if source == "B" and DeltaT < DT1:
            NewState = CC_2
        return NewState

    def CC_2(source, timestamp, DeltaT, NewState):
        if source == "A" and DeltaT >= DT2 and DeltaT < DT3:
            NewState = CC_3
        return NewState

    def CC_3(source, timestamp, DeltaT, NewState):
        if source == "A":
            CreateRecord("DN", carDn.day, carDn.StartTime, timestamp); NewState = UP_2
        elif source == "B" and DeltaT < DT1:
            CreateRecord("UP", carUp.day, carUp.StartTime, timestamp); NewState = DOWN_2
        return NewState
                
    def ERROR():
        pass

    NewState = IDLE;

    # =======================   main processing loop   ==============================

    # initialise
    Day = 1; LineNo = 0; NewState = IDLE; State = IDLE; PrevTime = 0
    DT1 = 72; DT2 = 72; DT3 = 2000; ms_in_day = 1000*60*60*24

    # process lines from STDIN
    for line in open('test-data.txt'):
        LineNo += 1
        State = NewState
        
        # decode 'source' and 'timestamp'
        source = line[0]; timestamp = long(line[1:-1])
        
        if timestamp < PrevTime:
            Day += 1
        DeltaT = (ms_in_day + timestamp  - PrevTime) % ms_in_day
        PrevTime = timestamp
        
        # handle the new event
        # print "State:", State.__name__, "Line:", LineNo, "Source:", source, "Time:", timestamp, "dT:", DeltaT
        NewState = State(source, timestamp, DeltaT, ERROR)
        
        # after sequence erre, report and re-ssync
        if NewState == ERROR:
            print "ERROR: State:", State.__name__, "Line:", LineNo, "Source:", source, "Time:", timestamp, "dT:", DeltaT
            RESYNC(source, timestamp, DeltaT, ERROR)
            if NewState == ERROR: NewState = IDLE
            
    if NewState <> IDLE:
        print "FINISH: State:", State.__name__, "Line:", LineNo, "Source:", source, "Time:", timestamp, "dT:", DeltaT

    exit