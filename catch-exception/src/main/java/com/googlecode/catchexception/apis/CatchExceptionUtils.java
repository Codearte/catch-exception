/**
 * Copyright (C) 2011 rwoo@gmx.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.catchexception.apis;

import com.googlecode.catchexception.CatchException;
import com.googlecode.catchexception.ExceptionNotThrownAssertionError;

class CatchExceptionUtils {
  public static void thenThrown(Class actualExceptionClazz) {
    Exception e = CatchException.caughtException();
    if (e == null) {
      // no exception caught -> assertion failed
      throw new ExceptionNotThrownAssertionError(actualExceptionClazz);
    } else if (!actualExceptionClazz.isAssignableFrom(CatchException
        .caughtException().getClass())) {
      // caught exception is of wrong type -> assertion failed
      throw new ExceptionNotThrownAssertionError(actualExceptionClazz, e);
    } else {
      // the caught exception is of the expected type -> nothing to do :-)
    }
  }
}
