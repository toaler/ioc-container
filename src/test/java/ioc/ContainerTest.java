package ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.bpt.ioc.bean.Dinner;
import org.bpt.ioc.bean.IceCream;
import org.bpt.ioc.bean.Potatoe;
import org.bpt.ioc.bean.Steak;
import org.bpt.ioc.component.EagerAutoWired;
import org.bpt.ioc.component.LazyAutoWired;
import org.bpt.ioc.component.LazyComponent;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ContainerTest {

	/**
	 * The purpose of this test is to validate how eager vs lazy auto-wired
	 * members of a component are dealt with. More specifically:
	 * 
	 * 1) Validate that a implicit, eager autowired member is eagerly
	 * constructed.
	 * 
	 * 2) Validate that a lazily designated autowired member is wrapped with a
	 * CGLIB proxy and constructed on first use.
	 * 
	 */
	@Test
	public void testLazyComponentEagerVsLazyAutowiredBehavior() {
		try (AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext()) {

			acac.scan("org.bpt.ioc.configuration.components");
			acac.refresh();

			BeanDefinition bd = acac.getBeanDefinition("LazyComponent");
			assertNotNull(bd);
			assertTrue(bd.isLazyInit());

			LazyComponent lc = acac.getBean("LazyComponent", LazyComponent.class);

			// V A L I D A T E   E A G E R   A U T O W I R E D   M E M B E R S
			
			StackTraceElement[] ste = lc.getEagerAutoWired().getCtorStack();
			
			// 0) Ensure EagerAutoWired is the correct non proxy type (non CGLIB)
			assertEquals(EagerAutoWired.class.getName(), lc.getEagerAutoWired().getClass().getName());

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
			
			// 3) Validate that LazyAutoWired method calls pass through cglib generated proxy
			assertTrue(hasOneFrameThatStartsWith(law.getAccessPath(), "org.springframework.cglib.proxy.MethodProxy.invoke("));
		}
	}
	
	@Test
	public void testConfigurationEagerVsLazyBeanInjectionBehavior() {
		try (AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext()) {
			acac.scan("org.bpt.ioc.configuration.beans");
			acac.refresh();

			Dinner d = acac.getBean("Dinner", Dinner.class);

			// V A L I D A T E   E A G E R   I N J E C T E D   B E A N S
			
			// 0) Ensure Steak/Potatoe is the correct non proxy type (non CGLIB)
			assertEquals(Steak.class.getName(), d.getSteak().getClass().getName());
			assertEquals(Potatoe.class.getName(), d.getPotatoe().getClass().getName());

			StackTraceElement[] ste = d.getSteak().getCtorStack();

			// 1) Edge @Bean Dinner is eager which is created when ACAC.refresh is called
			assertTrue(hasOneFrameThatStartsWith(ste,
					"org.springframework.context.support.AbstractApplicationContext.refresh"));

			// 2) Steak was autowired as it was a eagerly injected bean to Dinner
			assertTrue(hasOneFrameThatStartsWith(ste,
					"org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod"));

			// 3) Validated eagerly injected @Bean Steak is constructed
			assertTrue(hasOneFrameThatStartsWith(ste, "org.bpt.ioc.bean.Steak.<init>"));

			// 4) Runtime check to validate Steak is not a proxy
			assertFalse(hasOneFrameThatStartsWith(ste, "org.springframework.cglib.proxy.MethodProxy.invoke("));
			
			// V A L I D A T E   L A Z Y   I N J E C T E D   B E A N S
			
			// Get lazily injected IceCream
			IceCream ic = d.getIceCream();

			// 0) Ensure LazyAutoWired is the correct proxy type (CGLIB)
			assertTrue(ic.getClass().getName().startsWith("org.bpt.ioc.bean.IceCream$$EnhancerBySpringCGLIB"));

			ste = ic.getCtorStack(); // First access to wrapped IceCream instance.

			// 1) Validate construction happened on first access.
			assertTrue(hasOneFrameThatEndsWith(ste, "getCtorStack(<generated>)"));

			// 2) Validated that lazy constructed object type
			assertTrue(hasOneFrameThatStartsWith(ste, "org.bpt.ioc.bean.IceCream.<init>"));

			// 3) Validate that IceCream method calls pass through cglib generated proxy
			assertTrue(hasOneFrameThatStartsWith(ic.getAccessPath(), "org.springframework.cglib.proxy.MethodProxy.invoke("));

		}
	}

	private boolean hasOneFrameThatStartsWith(StackTraceElement[] ste, String startsWith) {
		return Arrays.stream(ste).filter(s -> s.toString().startsWith(startsWith)).findAny().isPresent();
	}
	
	private boolean hasOneFrameThatEndsWith(StackTraceElement[] ste, String suffix) {
		return Arrays.stream(ste).filter(s -> s.toString().endsWith(suffix)).findAny().isPresent();
	}

}
