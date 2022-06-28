package com.creativedrive.security.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@ConfigurationProperties(prefix = "jwt.config")
@Getter
@Setter
@ToString
public class JwtConfiguration 
{
	private String url = "/login/**";

//    @NestedConfigurationProperty
//    private Header header = new Header();
    
    private String header_name = "Authorization";
    
    private String prefix = "Bearer";

    private int expiration = 3600;

    //site de geração da chave de 32 bits (http://www.unit-conversion.info/texttools/random-string-generator/)
    private String privateKey = "d8bPT2fmPKiEDCmcufiERgCUZRieMRoE";
    private String type = "encrypted";

    @Getter
    @Setter
    public static class Header
    {
        private String name = "Authorization";
        private String prefix = "Bearer";
    }
}