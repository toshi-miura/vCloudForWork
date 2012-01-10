package work;

/**
 * 継承して、設定ファイルから読むなり、固定値で決めるなり実装する。
 * 定義されているフィールドすべてに値を与えること。
 *
 * @author user
 *
 */
public abstract class VcdConf {

	public String vcdName;
	public String adBaseDN;
	public String ldapADsURL;
	public String domainName;
	public String PnoInvalidTaskTemplatePath;
	public String AuthInvalidTaskTemplatePath;
	public String SendCostMailTaskTemplatePath;
	public String TaskRunDir;

}
