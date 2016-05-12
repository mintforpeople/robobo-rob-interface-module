package com.mytechia.robobo.rob.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mytechia.robobo.framework.activity.DefaultRoboboActivity;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;

public class MainActivity extends DefaultRoboboActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configureLogback();
        setDisplayActivityClass(RobMovementActivity.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void startRoboboApplication() {

    }



    private void configureLogback(){

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        loggerContext.reset();


        Logger root = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);


        PatternLayoutEncoder patternLayout = new PatternLayoutEncoder();
        patternLayout.setContext(loggerContext);
        patternLayout.setPattern("%d{dd/MM/yyyy-HH:mm:ss} %-5p: %-20c{1} - %m%n");
        patternLayout.start();

        ConsoleAppender consoleAppender= new ConsoleAppender();
        consoleAppender.setName("default-console");
        consoleAppender.setEncoder(patternLayout);
        consoleAppender.start();

        root.setLevel(Level.TRACE);

        root.addAppender(consoleAppender);

    }


}
