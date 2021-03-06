/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2018 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.enterprise.concurrent.test;

import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    private static final long MAX_WAIT_TIME = 10000L; // 10 seconds

    public static interface BooleanValueProducer {
      public boolean getValue();
    }

    public static boolean waitForBoolean(BooleanValueProducer valueProducer, boolean expectedValue, String loggerName) {
      long endWaitTime = System.currentTimeMillis() + MAX_WAIT_TIME;
      boolean value = valueProducer.getValue();
      while ( (value != expectedValue) &&
              endWaitTime > System.currentTimeMillis()) {
          try {
              Thread.sleep(100);
          } catch (InterruptedException ex) {
              Logger.getLogger(loggerName).log(Level.SEVERE, null, ex);
          }
        value = valueProducer.getValue();
      }
      return value;      
    }

    public static boolean waitForTaskStarted(final Future<?> future, final ManagedTaskListenerImpl listener, String loggerName) {
      return waitForBoolean(
          new BooleanValueProducer() {
            public boolean getValue() {
              return listener.eventCalled(future, listener.STARTING);
            }
          },
          true, loggerName);
    }

    public static boolean waitForTaskComplete(final RunnableImpl task, String loggerName) {
      return waitForBoolean(
          new BooleanValueProducer() {
            public boolean getValue() {
              return task.runCalled;
            }
          },
          true,loggerName);
    }

    public static boolean waitForTaskAborted(final Future<?> future, final ManagedTaskListenerImpl listener, String loggerName) {
      return waitForBoolean(
          new BooleanValueProducer() {
            public boolean getValue() {
              return listener.eventCalled(future, listener.ABORTED);
            }
          },
          true, loggerName);
    }

  public static boolean waitForTaskDone(final Future<?> future, final ManagedTaskListenerImpl listener, String loggerName) {
    return waitForBoolean(
        new BooleanValueProducer() {
          public boolean getValue() {
            return listener.eventCalled(future, listener.DONE);
          }
        },
        true, loggerName);
  }


    public static String generateName() {
        return new java.util.Date(System.currentTimeMillis()).toString();
    }

}
