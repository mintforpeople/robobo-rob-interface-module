/*******************************************************************************
 *
 *   Copyright 2016 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 *   Copyright 2016 Gervasio Varela <gervasio.varela@mytechia.com>
 *
 *   This file is part of Robobo Framework Library.
 *
 *   Robobo Framework Library is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Robobo Framework Library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Robobo Framework Library.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

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

/** Custom Robobo application
 *
 * @author Gervasio Varela
 */
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
