package com.bargetor.nest.bpc.exception;

/**
 * Created by Bargetor on 16/4/13.
 */
public class BPCException extends RuntimeException {

    /**
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     *
     * @since 1.0.0
     */

    private static final long serialVersionUID = 1L;
    private int status;
    private String msg;

    /**
     * 创建一个新的实例 BPCResponseError.
     *
     */
    public BPCException() {
        // TODO Auto-generated constructor stub
    }

    public BPCException(Exception e) {
        super(e);
    }

    public BPCException(String msg) {
        super(msg);
    }

    public BPCException(Exception e, String msg) {
        super(msg, e);
    }

    public BPCException(int status, String msg){
        super(msg);
        this.status = status;
        this.msg = msg;
    }
    /**
     * status
     *
     * @return  the status
     * @since   1.0.0
     */

    public int getStatus() {
        return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }
    /**
     * msg
     *
     * @return  the msg
     * @since   1.0.0
     */

    public String getMsg() {
        return msg;
    }
    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
