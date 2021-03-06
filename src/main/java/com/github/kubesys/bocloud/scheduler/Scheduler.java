/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.bocloud.scheduler;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kubesys.KubernetesClient;
import com.github.kubesys.KubernetesWatcher;

/**
 * @author wuheng
 * @since 2019.4.20
 */
public class Scheduler {

	protected final KubernetesClient client;
	
	protected final static List<String> nodes = new ArrayList<>();
	
	public Scheduler(KubernetesClient client) {
		this.client = client;
	}

	public void start() throws Exception {
		client.watchResources("Node", new NodeChangedWatcher(client));
		client.watchResources("Pod", new doScheduling(client));
	}

	public static class doScheduling extends KubernetesWatcher {

		public doScheduling(KubernetesClient kubeClient) {
			super(kubeClient);
		}

		@Override
		public void doAdded(JsonNode node) {
			// not use this scheduler
			if (!getScheduler(node).equals("bocloud-schduler")
					// pod has scheduled
					|| getNodeName(node) != null) {
				// ignore here
				return;
			}
			
			// please extends it according your demands
			
			// get pod name
			String pod  = getName(node);
			
			// select a node
			String host = selectNode();
			
			try {
				
				// finish scheduling
				JsonNode binding = getBinding(pod, host);
				kubeClient.bindingResource(binding);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		protected JsonNode getBinding(String pod, String host) {
			ObjectNode binding = new ObjectMapper().createObjectNode();
			binding.put("apiVersion", "v1");
			binding.put("kind", "Binding");
			
			ObjectNode metadata = new ObjectMapper().createObjectNode();
			metadata.put("name", pod);
			binding.set("metadata", metadata);
			
			ObjectNode target = new ObjectMapper().createObjectNode();
			target.put("apiVersion", "v1");
			target.put("kind", "Node");
			target.put("name", host);
			binding.set("target", target);
			
			return binding;
		}

		protected String selectNode() {
			String host = nodes.get(0);
			return host;
		}

		@Override
		public void doModified(JsonNode node) {
			
		}

		@Override
		public void doDeleted(JsonNode node) {
			if (!getScheduler(node).equals("bocloud-schduler")
					|| getNodeName(node) != null) {
				return;
			}
		}

		@Override
		public void doClose() {
			
		}
		
		protected String getScheduler(JsonNode node) {
			return node.get("spec").get("schedulerName").asText();
		}
		
		protected String getNodeName(JsonNode node) {
			return !node.get("spec").has("nodeName") ? null :
					node.get("spec").get("nodeName").asText();
		}
	}
	
	
	public static class NodeChangedWatcher extends KubernetesWatcher {

		public NodeChangedWatcher(KubernetesClient kubeClient) {
			super(kubeClient);
		}

		@Override
		public void doAdded(JsonNode node) {
			nodes.add(getName(node));
		}

		@Override
		public void doModified(JsonNode node) {
			
		}

		@Override
		public void doDeleted(JsonNode node) {
			nodes.remove(getName(node));
		}

		@Override
		public void doClose() {
			
		}
		
		
	}
	
	public static String getName(JsonNode node) {
		return node.get("metadata").get("name").asText();
	}
}
