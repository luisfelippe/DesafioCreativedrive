package com.creativedrive.security.service;

import javax.servlet.http.HttpServletResponse;

public class JWTTokenAutenticacaoService 
{
	//realiza a liberação contra erro de Cors no navegador
	public static void liberacaoCors(HttpServletResponse response)
	{
		if(response.getHeader("Access-Control-Allow-Origin") == null)
		{
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Headers") == null)
		{
			response.addHeader("Access-Control-Allow-Headers", "*");
		}		
		
		if(response.getHeader("Access-Control-Request-Headers") == null)
		{
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Methods") == null)
		{
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
	}
}
