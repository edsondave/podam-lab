package co.edu.uniandes.testautomation.podam_lab.test;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class TestConfiguration {

    public URL getServiceUrl () throws MalformedURLException {
        return new URL("http://localhost:4723/wd/hub");
    }

}
