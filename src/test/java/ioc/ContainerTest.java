package ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.bpt.ioc.component.LazyAutoWired;
import org.bpt.ioc.component.LazyComponent;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ContainerTest {

	@Test
	public void testLazyComponentEagerVsLazyAutowiredBehavior() {
		try (AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext()) {

			acac.scan("org.bpt.ioc.configuration");
			acac.refresh();

			BeanDefinition bd = acac.getBeanDefinition("LazyComponent");
			assertNotNull(bd);
			assertTrue(bd.isLazyInit());

			LazyComponent lc = acac.getBean("LazyComponent", LazyComponent.class);

			// V A L I D A T E   E A G E R   A U T O W I R E D   M E M B E R S
			
			StackTraceElement[] ste = lc.getEagerAutoWired().getCtorStack();
			
			// 0) Ensure EagerAutoWired is the correct non proxy type (non CGLIB)
			assertEquals("org.bpt.ioc.component.EagerAutoWired", lc.getEagerAutoWired().getClass().getName());

			// 1) Edge component is lazy which is created when ACAC.getBean is called
			assertTrue(hasOneFrameThatStartsWith(ste,
					"org.springframework.context.support.AbstractApplicationContext.getBean"));

			// 2) All eager @Autowired members will be resolved when edge bean is created
			assertTrue(hasOneFrameThatStartsWith(ste,
					"org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateBean"));

			// 3)  Resolved @Autowired members will be constructed
			assertTrue(hasOneFrameThatStartsWith(ste, "org.bpt.ioc.component.EagerAutoWired.<init>"));
			
			// 4)  Runtime check to validate EagerAutoWire is not a proxy
			assertFalse(hasOneFrameThatStartsWith(ste, "org.springframework.cglib.proxy.MethodProxy.invoke"));

			// V A L I D A T E   L A Z Y   A U T O W I R E D   M E M B E R S
			LazyAutoWired law = lc.getLazyAutoWired();
			
			
			// 0) Ensure LazyAutoWired is the correct proxy type (CGLIB)
			assertTrue(law.getClass().getName()
					.startsWith("org.bpt.ioc.component.LazyAutoWired$$EnhancerBySpringCGLIB"));
			
			ste = law.getCtorStack();  // First access

			// 1) Validate construction happened on first access.
			assertTrue(hasOneFrameThatEndsWith(ste, "getCtorStack(<generated>)"));
			
			// 2)  Validated that lazy constructed object type
			assertTrue(hasOneFrameThatStartsWith(ste, "org.bpt.ioc.component.LazyAutoWired.<init>"));
			

		}

	}

	private boolean hasOneFrameThatStartsWith(StackTraceElement[] ste, String startsWith) {
		return Arrays.stream(ste).filter(s -> s.toString().startsWith(startsWith)).findAny().isPresent();
	}
	
	private boolean hasOneFrameThatEndsWith(StackTraceElement[] ste, String suffix) {
		return Arrays.stream(ste).filter(s -> s.toString().endsWith(suffix)).findAny().isPresent();
	}

}
