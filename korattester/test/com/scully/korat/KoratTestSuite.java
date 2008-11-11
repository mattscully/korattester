package com.scully.korat;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.scully.korat.finitization.FinitizationTest;
import com.scully.korat.finitization.IntArraySetTest;
import com.scully.korat.instrument.InstrumenterTest;
import com.scully.korat.map.BeanXmlMapperTest;
import com.scully.korat.map.StateSpaceBeanTest;
import com.scully.korat.test.SearchTreeInstrumentTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
    CandidateStateTest.class
    ,FinitizationTest.class
    ,StateSpaceBeanTest.class
    ,BeanXmlMapperTest.class
    ,InstrumenterTest.class
    ,IntArraySetTest.class
    ,KoratMainTest.class
    ,SearchTreeInstrumentTest.class
})
public class KoratTestSuite
{

    public static Test suite()
    {
        return new JUnit4TestAdapter(KoratTestSuite.class);
    }

}
