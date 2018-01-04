package com.BamoOS.Modules.ProcessManager;

import com.BamoOS.Modules.Communication.Sms;
import com.BamoOS.Modules.MemoryManagment.PageTable;
import com.BamoOS.Modules.Processor.IProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PCB {
	
    public enum State{
        ACTIVE,
        WAITING,
    	READY,
    	FINISHED
    }

    public enum Register{
    	A,
    	B,
    	C,
		D
    }
	
	//Id procesu
	private int PID;
	//Stan procesu, wstrzymany, gotowy itd
	private State ProcessState;
	//Nazwa procesu
	private String ProcessName;
	//Id grupy do kt�rej nale�y proces
	private int PGID;
	//Rejestry
	private int A;
	private int B;
	private int C;
	private int D;
	//Pola dla interpretera oraz cpu
	private int Counter;
	private int Timer;
	private double Tau;
	public PageTable pageTable;
	private ArrayList<Sms> SmsList;
	private int LastSenderID;
	private Map<Integer,Integer> mapLine = new HashMap<>();
	private ProcessManager PM;

	
	public PCB(int ProcessID, String ProcessName, int ProcessGroup) {
		this.PID = ProcessID;
		this.PGID = ProcessGroup;
	 	this.ProcessName = ProcessName;
		this.A = 0;
		this.B = 0;
		this.C = 0;
		this.Counter = 0;
		this.Timer = 0;
		this.ProcessState = State.READY;
        mapLine = new HashMap<>();
	}

	public PCB(int ProcessID, String ProcessName, int ProcessGroup, PageTable pt, Map ml) {
		this.PID = ProcessID;
		this.PGID = ProcessGroup;
		this.ProcessName = ProcessName;
		this.A = 0;
		this.B = 0;
		this.C = 0;
		this.Counter = 0;
		this.Timer = 0;
		this.ProcessState = State.READY;
		this.pageTable = pt;
		this.mapLine = ml;
	}
	
	public int getPID() {
		return this.PID;
	}
	
	public int getPGID() {
		return this.PGID;
	}
	
	public String getName() {
		return this.ProcessName;
	}
	
	public State getState() {
		return this.ProcessState;
	}
	
	public void setState(State state) {
		this.ProcessState = state;
	}
	
	public int getRegister(Register register) {
		switch(register) {
		case A:
			return this.A;
		case B:
			return this.B;
		case C:
			return this.C;
		case D:
			return this.D;
		}
		return A;
	}
	
	public void setRegister(Register register, int val) {
		switch(register) {
		case A:
			this.A = val;
		case B:
			this.B = val;
		case C:
			this.C = val;
			case D:

		}
	}
	
	public int getCounter() {
		return this.Counter;
	}
	
	public void setCounter(int counter) {
		this.Counter = counter;
	}

	public int getTimer() {
		return Timer;
	}
	
	public void setTimer(int timmer) {
		this.Timer = timmer;
	}
	
	public void printInfo() {
		System.out.println("PID"+'\t'+"PGID"+'\t'+"A"+'\t'+"B"+'\t'+"C"+'\t'+"Licznik"+'\t'+"Timer"+'\t'+"Tau"+'\t'+"Stan"+'\t'+"Nazwa");
		System.out.println("--------------------------------------------------------------------------------");
		System.out.print(this.getPID()+"\t"+this.getPGID()+"\t");
		System.out.print(this.getRegister(Register.A)+"\t"+this.getRegister(Register.B)+"\t"+this.getRegister(Register.C)+"\t");
		System.out.println(this.getCounter()+"\t"+this.getTimer()+"\t"+this.getTau()+"\t"+this.getState()+"\t"+this.getName());
//		try {
//			PM.findConditionVariable(this.PGID).printInfo();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	//Gettery i Settery dla p�l wiadomo�ci, interpretera oraz cpu

	public ArrayList<Sms> getSmsList() {
		return SmsList;
	}

	public void setSmsList(ArrayList<Sms> smsList) {
		SmsList = smsList;
	}

	public double getTau() {
		return Tau;
	}

	public void setTau(double tau) {
		Tau = tau;
	}

	public int getLastSenderID() {
		return LastSenderID;
	}

	public void setLastSenderID(int lastSenderID) {
		LastSenderID = lastSenderID;
	}

	public Map<Integer, Integer> getMapLine() {
		return mapLine;
	}

	public void setMapLine(Map<Integer, Integer> mapLine) {
		this.mapLine = mapLine;
	}
}
