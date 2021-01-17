package com.fc.v2.common.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 * 
 * @author fuce
 */
@Component
@ConfigurationProperties(prefix = "fuce")
public class V2Config
{
    /** 项目名称 */
    private String name;
    /** 版本 */
    private String version;
    /** 版权年份 */
    private String copyrightYear;
    /** 邮箱发送smtp */
    private static String emailSmtp;
    /** 发送邮箱端口 */
    private static String emailPort;
    /** 发送邮箱登录账号 */
    private static String emailAccount;
    /** 发送邮箱登录密码 */
    private static String emailPassword;
    /** 演示模式 **/
    private static String demoEnabled;
    /** 滚动验证码 **/
    private static Boolean rollVerification;
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getCopyrightYear()
    {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear)
    {
        this.copyrightYear = copyrightYear;
    }
    
	public static String getEmailSmtp() {
		return emailSmtp;
	}

	public static void setEmailSmtp(String emailSmtp) {
		V2Config.emailSmtp = emailSmtp;
	}

	public static String getEmailPort() {
		return emailPort;
	}

	public static void setEmailPort(String emailPort) {
		V2Config.emailPort = emailPort;
	}

	public static String getEmailAccount() {
		return emailAccount;
	}

	public static void setEmailAccount(String emailAccount) {
		V2Config.emailAccount = emailAccount;
	}

	public static String getEmailPassword() {
		return emailPassword;
	}

	public static void setEmailPassword(String emailPassword) {
		V2Config.emailPassword = emailPassword;
	}

	public static String getDemoEnabled() {
		return demoEnabled;
	}

	public void setDemoEnabled(String demoEnabled) {
		V2Config.demoEnabled = demoEnabled;
	}

	public static Boolean getRollVerification() {
		return rollVerification;
	}

	public void setRollVerification(Boolean rollVerification) {
		V2Config.rollVerification = rollVerification;
	}
	
	
	
}
