/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.bocloud.scheduler;

import com.github.kubesys.KubernetesClient;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * 
 * @version 2.3.0
 * @since 2020.2.15
 * 
 **/
public class Main {
	
	
	/*****************************************************************************************
	 * 
	 * Main
	 * 
	 *****************************************************************************************/

	/**
	 * @param args                               args
	 * @throws Exception                         exception
	 */
	public static void main(String[] args) throws Exception {
	
		// kubeUrl : https://IP:6443
		// token: see README.md
		KubernetesClient kubeClient = new KubernetesClient(
									System.getenv("kubeUrl"), 
									System.getenv("token"));
		
		new Scheduler(kubeClient).start();
	}
	
}
