package com.scully.korat.test;

import java.util.Iterator;
import java.util.List;

import com.scully.korat.KoratEngine;
import com.scully.korat.map.CandidateStateDTO;

public class FailedObjectIterator<E> implements Iterator<E>
{
    private KoratEngine koratEngine;
    private Iterator<CandidateStateDTO> iterator;
    
    public FailedObjectIterator(KoratEngine koratEngine, List<CandidateStateDTO> failedCandidates)
    {
        this.koratEngine = koratEngine;
        this.iterator = failedCandidates.iterator();
    }

    public boolean hasNext()
    {
        return this.iterator.hasNext();
    }

    @SuppressWarnings("unchecked")
    public E next()
    {
        CandidateStateDTO state = this.iterator.next();
        this.koratEngine.setCandidateState(state);
        return (E) this.koratEngine.getRootObject();
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
