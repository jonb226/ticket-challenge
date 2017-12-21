package com.challenge.tickets;

import java.util.Objects;

public class OpaqueStringId{
    private String id;

    protected OpaqueStringId(String id){
        this.id = id;
    }

    public String stringValue(){
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpaqueStringId id1 = (OpaqueStringId) o;
        return Objects.equals(id, id1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
