package com.flow.platform.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Only include basic properties of command
 * <p>
 * Created by gy@fir.im on 20/05/2017.
 * Copyright fir.im
 */
public abstract class CmdBase extends Jsonable {

    /**
     * Destination of command
     * Agent name can be null, that means platform select a instance to run cmd
     */
    protected AgentPath agentPath;

    /**
     * Command type (Required)
     */
    protected CmdType type;

    /**
     * Command content (Required when type = RUN_SHELL)
     */
    protected String cmd;

    /**
     * Cmd timeout in seconds
     */
    protected Integer timeout;

    /**
     * Reserved field for cmd queue
     */
    protected Integer priority;

    /**
     * Platform will reserve a machine for session
     */
    protected String sessionId;

    /**
     * Input parameter, deal with export XX=XX before cmd execute
     * Add input: getInputs().add(key, value)
     */
    protected Map<String, String> inputs = new HashMap<>();

    /**
     * Cmd working dir, default is user.home
     */
    protected String workingDir;

    /**
     * Filter for env input to CmdResult.output map
     */
    protected String outputEnvFilter;

    /**
     * Url for report cmd status to other system
     */
    protected String webhook;

    public CmdBase() {
    }

    public CmdBase(String zone, String agent, CmdType type, String cmd) {
        this(new AgentPath(zone, agent), type, cmd);
    }

    public CmdBase(AgentPath agentPath, CmdType type, String cmd) {
        this.agentPath = agentPath;
        this.type = type;
        this.cmd = cmd;
    }

    public AgentPath getAgentPath() {
        return agentPath;
    }

    public void setAgentPath(AgentPath agentPath) {
        this.agentPath = agentPath;
    }

    public String getZoneName() {
        if (agentPath == null) {
            return null;
        }
        return agentPath.getZone();
    }

    public String getAgentName() {
        if (agentPath == null) {
            return null;
        }
        return agentPath.getName();
    }

    public CmdType getType() {
        return type;
    }

    public void setType(CmdType type) {
        this.type = type;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean hasSession() {
        return sessionId != null;
    }

    public Map<String, String> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, String> inputs) {
        this.inputs = inputs;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    public String getOutputEnvFilter() {
        return outputEnvFilter;
    }

    public void setOutputEnvFilter(String outputEnvFilter) {
        this.outputEnvFilter = outputEnvFilter;
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CmdBase cmdBase = (CmdBase) o;

        if (!agentPath.equals(cmdBase.agentPath)) return false;
        if (type != cmdBase.type) return false;
        if(cmd == null){
            return cmd == cmdBase.cmd;
        }
        return cmd.equals(cmdBase.cmd);
    }

    @Override
    public int hashCode() {
        int result = agentPath.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + cmd.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CmdBase{" +
                " zone=" + (agentPath == null ? "null" : agentPath.getZone()) +
                ", agent=" + (agentPath == null ? "null" : agentPath.getName()) +
                ", type=" + type +
                ", cmd='" + cmd + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}