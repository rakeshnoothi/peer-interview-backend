package com.rakesh.peer_interview.signaler;

import java.util.Map;

public class SignalingDataDTO {
	 	private String type;  
	    private String from;  
	    private String to;    
	    private Map<String, Object> description; 
	    private Map<String, Object> candidate;
	    private String role;
	    
	    public SignalingDataDTO() {}
	    
		public SignalingDataDTO(
				String type, 
				String from, 
				String to, 
				Map<String, Object> description,
				Map<String, Object> candidate,
				String role) {
			this.type = type;
			this.from = from;
			this.to = to;
			this.description = description;
			this.candidate = candidate;
			this.role = role;		}

		// Getters and Setters
	    public String getType() {
	        return type;
	    }

	    public void setType(String type) {
	        this.type = type;
	    }

	    public String getFrom() {
	        return from;
	    }

	    public void setFrom(String from) {
	        this.from = from;
	    }

	    public String getTo() {
	        return to;
	    }

	    public void setTo(String to) {
	        this.to = to;
	    }

	    public Map<String, Object> getDescription() {
	        return description;
	    }

	    public void setDescription(Map<String, Object> description) {
	        this.description = description;
	    }

	    public Map<String, Object> getCandidate() {
	        return candidate;
	    }

	    public void setCandidate(Map<String, Object> candidate) {
	        this.candidate = candidate;
	    }

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		@Override
		public String toString() {
			return "SignalingDataDTO [type=" + type + ", from=" + from + ", to=" + to + ", description=" + description
					+ ", candidate=" + candidate + ", role=" + role + "]";
		}

	    
}
