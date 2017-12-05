/*-
 * #%L
 * Tutorials Window
 * %%
 * Copyright (C) 2017 SnapGames
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
/**
 * SnapGames
 *
 * @year 2016
 *
 */
package com.snapgames.gdj.core.state.factory;

/**
 * <p>
 * NoDefaultStateException
 * <p>
 * This exception is raised when no default state is declared in the
 * <code>story.xml</code> file.
 * 
 * 
 * @author Frédéric Delorme
 *
 */
@SuppressWarnings("serial")
public class NoDefaultStateException extends Exception {

	public NoDefaultStateException() {
		super();
	}

	public NoDefaultStateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoDefaultStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoDefaultStateException(String message) {
		super(message);
	}

	public NoDefaultStateException(Throwable cause) {
		super(cause);
	}
}
