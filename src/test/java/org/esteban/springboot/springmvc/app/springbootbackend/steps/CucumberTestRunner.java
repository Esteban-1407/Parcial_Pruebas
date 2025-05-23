package org.esteban.springboot.springmvc.app.springbootbackend.steps;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports.html")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "org.esteban.springboot.springmvc.app.springbootbackend")
@ConfigurationParameter(key = SNIPPET_TYPE_PROPERTY_NAME, value = "camelcase")
public class CucumberTestRunner {
}
