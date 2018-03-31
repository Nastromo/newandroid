package com.face_location.facelocation;

/**
 * Created by admin on 30.11.17.
 */

public class Group {

    String groupName, groupMembers;
    int membersQuantity;

    public Group(String groupName, String groupMembers) {
        this.groupName = groupName;
        this.groupMembers = groupMembers;
    }

    public Group(String groupName, String groupMembers, int membersQuantity) {
        this.groupName = groupName;
        this.groupMembers = groupMembers;
        this.membersQuantity = membersQuantity;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(String groupMembers) {
        this.groupMembers = groupMembers;
    }

    public int getMembersQuantity() {
        return membersQuantity;
    }

    public void setMembersQuantity(int membersQuantity) {
        this.membersQuantity = membersQuantity;
    }
}
