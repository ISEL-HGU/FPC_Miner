package edu.handong.csee.isel.fcminer.saresultminer.git;

public class ChangeInfo {
	String dir = "";
	String changeType = "";
	int oldRange = 0, oldStart = 0, oldEnd=0, newStart=0, newRange=0, newEnd=0;
	String changedCode = "";
	
	public ChangeInfo() {
	
	}
	
	public ChangeInfo(String dir, String changeType, int oldStart, int oldRange, int oldEnd, int newStart, int newRange, int newEnd, String changedCode) {
		this.dir = dir;
		this.oldStart = oldStart;
		this.oldRange = oldRange;
		this.oldEnd = oldEnd;
		this.newStart = newStart;
		this.newRange = newRange;
		this.newEnd = newEnd;
		this.changedCode = changedCode;
		this.changeType = changeType;
	}
	
	public String getChangeType() {
		return changeType;
	}
	
	public String getDir() {
		return dir;
	}
	
	public int getOldStart() {
		return oldStart;
	}
	
	public int getOldEnd() {
		return oldEnd;
	}
	
	public int getOldRange() {
		return oldRange;
	}
	
	public int getNewStart() {
		return newStart;
	}
	
	public int getNewEnd() {
		return newEnd;
	}
	
	public int getNewRange() {
		return newRange;
	}
	
	public String getChangedCode() {
		return changedCode;
	}
}
