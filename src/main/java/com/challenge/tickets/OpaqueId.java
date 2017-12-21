package com.challenge.tickets;

import java.util.Objects;

public abstract class OpaqueId<T>{
    private T id;

    protected OpaqueId(T id){
        this.id = id;
    }

    public T value(){
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpaqueId id1 = (OpaqueId) o;
        return Objects.equals(id, id1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
