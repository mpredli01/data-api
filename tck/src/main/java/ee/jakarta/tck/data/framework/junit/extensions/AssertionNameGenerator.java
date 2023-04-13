/*
 * Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package ee.jakarta.tck.data.framework.junit.extensions;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayNameGenerator;

import ee.jakarta.tck.data.framework.junit.anno.Assertion;
import ee.jakarta.tck.data.framework.junit.anno.Standalone;
import ee.jakarta.tck.data.framework.utilities.TestProperty;

/**
 * <p>This custom JUnit name generator will append the {@code @Assertion.id}
 * if one exists to the name of the test method at runtime.</p>
 * 
 * <p>This custom JUnit name generator will also append the platform 
 * the test ran on by using the {@link ee.jakarta.tck.data.framework.utilities.TestProperty#platform}.</p>
 * 
 * @see ee.jakarta.tck.data.framework.junit.anno.Assertion
 * @see ee.jakarta.tck.data.framework.junit.anno.Standalone
 */
public class AssertionNameGenerator extends DisplayNameGenerator.Simple implements DisplayNameGenerator {
    
    private String platform = TestProperty.platform.getValue();
    private boolean isStandalone = TestProperty.platform.getBoolean();
    
    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        //If user provided a platform, then use that
        if(platform != null && !platform.isEmpty()) {
            return testClass.getSimpleName() + "#" + platform.replace("#", "");
        }
        
        //If standalone test is going to be deployed to a server make it clear
        if(testClass.isAnnotationPresent(Standalone.class) && !isStandalone) {
            return testClass.getSimpleName() + "#Deployed";
        }
        
        //Otherwise, use the super class
        return super.generateDisplayNameForClass(testClass);
    }
    
    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        //If user provided a platform, then use that
        if(platform != null && !platform.isEmpty()) {
            return nestedClass.getSimpleName() + "#" + platform.replace("#", "");
        }
        
        //If standalone test is going to be deployed to a server make it clear
        if(nestedClass.isAnnotationPresent(Standalone.class) && !isStandalone) {
            return nestedClass.getSimpleName() + "#Deployed";
        }
        
        //Otherwise, use the super class
        return super.generateDisplayNameForNestedClass(nestedClass);
    }
    
    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        
        Assertion instance = testMethod.getAnnotation(Assertion.class);
        if(instance != null) {
            return testMethod.getName() + "@Assertion.id:" + instance.id();
        }
        return super.generateDisplayNameForMethod(testClass, testMethod);
    }
}
