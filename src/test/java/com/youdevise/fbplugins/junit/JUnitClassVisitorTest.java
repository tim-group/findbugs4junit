/*
 * FindBugs4JUnit. Copyright (c) 2011 youDevise, Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
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
*/

package com.youdevise.fbplugins.junit;

import static org.hamcrest.collection.IsCollectionContaining.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.objectweb.asm.ClassReader;

import com.youdevise.fbplugins.junit.benchmarks.ManyIgnoredOneActive;
import com.youdevise.fbplugins.junit.benchmarks.OneCommentedOutIgnoreTestCase;
import com.youdevise.fbplugins.junit.benchmarks.OneIgnoredOneActive;
import com.youdevise.fbplugins.junit.benchmarks.OneIgnoredTestCase;
import com.youdevise.fbplugins.junit.impl.JUnitTestVisitor;

public class JUnitClassVisitorTest {

	
	@Test public void
	hasFoundIgnore() throws Exception {
		UnitTestVisitor visitor = runDetector(OneIgnoredTestCase.class);
		assertTrue("Should have found @Ignore'd test.", visitor.classContainsIgnoredTests());
	}
	
	
	@Test public void
	reportsDetailsOfIgnoredTests() throws Exception {
		UnitTestVisitor visitor = runDetector(OneIgnoredOneActive.class);

		List<IgnoredTestDetails> detailsOfIgnoredTests = visitor.detailsOfIgnoredTests();
		assertThat(detailsOfIgnoredTests.size(), is(1));
		assertThat(detailsOfIgnoredTests, hasItem(new IgnoredTestDetails(35, "myIgnoredTest", "OneIgnoredOneActive.java")));

	}
	
	@Test public void
	reportsOnManyIgnoredTests() throws Exception {
		UnitTestVisitor visitor = runDetector(ManyIgnoredOneActive.class);
		List<IgnoredTestDetails> detailsOfIgnoredTests = visitor.detailsOfIgnoredTests();
		
		assertThat(detailsOfIgnoredTests.size(), is(2));
		assertThat(detailsOfIgnoredTests, hasItem(new IgnoredTestDetails(36, "myIgnoredTest", "ManyIgnoredOneActive.java")));
		assertThat(detailsOfIgnoredTests, hasItem(new IgnoredTestDetails(42, "mySecondIgnoredTest", "ManyIgnoredOneActive.java")));
	}
	
	@Test public void
	doesNotReportIgnoredTestWhenTheIgnoreAnnotationIsCommentedOut() {
		UnitTestVisitor visitor = runDetector(OneCommentedOutIgnoreTestCase.class);
		List<IgnoredTestDetails> detailsOfIgnoredTests = visitor.detailsOfIgnoredTests();
		
		assertThat(detailsOfIgnoredTests.size(), is(0));
	}

	
	
	private UnitTestVisitor runDetector(Class<?> toVisit) {
		JUnitTestVisitor visitor = new JUnitTestVisitor();
		ClassReader cr;
		try {
			cr = new ClassReader(toVisit.getName());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		cr.accept(visitor, 0);
		return visitor;
	}
	
}
