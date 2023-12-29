package ru.vsu.cs.team4.task4.objio.exceptions;

public class ObjReaderException extends RuntimeException {
    public ObjReaderException(String errorMessage, int lineIndex) {
        super("Error parsing OBJ file on line: " + lineIndex + ". " + errorMessage);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ObjReaderException)) {
            return false;
        }

        return this.getMessage().equals(((ObjReaderException) obj).getMessage());
    }
}