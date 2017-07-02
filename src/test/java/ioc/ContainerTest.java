package ioc;

import org.bpt.ioc.bean.A;
import org.bpt.ioc.component.B;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class ContainerTest {

	@Test
	public void testConfiguration() {
		AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext();
		acac.scan("org.bpt.ioc.configuration");
		acac.refresh();

		A a = acac.getBean("A", A.class);

		assertThat(a.getClass()).isEqualTo(A.class);
		assertThat(a.getName()).isEqualTo("a");
		
		B b = acac.getBean("B", B.class);

		assertThat(b.getClass()).isEqualTo(B.class);
		assertThat(b.getName()).isEqualTo("b");
	}
}
