package all;

import work.CalcConf;
import work.CalcPayment;
import work.CalcPaymentImpl;
import work.Controller;
import work.ProjCodeChecker;
import work.ProjCodeCheckerImpl;
import work.VcdConf;
import work.VcdConfImpl;
import work.WorkCalcConf;

import com.google.inject.AbstractModule;

public class TestModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(CalcPayment.class).to(CalcPaymentImpl.class);
		bind(ProjCodeChecker.class).to(ProjCodeCheckerImpl.class);
		bind(CalcConf.class).to(WorkCalcConf.class);
		bind(Controller.class);
		bind(VcdConf.class).to(VcdConfImpl.class);

	}
}
