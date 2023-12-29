package ru.vsu.cs.team4.task4.objio.exceptions;

public class GroupNameException extends ObjReaderException {
    public GroupNameException(int lineIndex) {
        super("Group must have a name.", lineIndex);
    }
}
