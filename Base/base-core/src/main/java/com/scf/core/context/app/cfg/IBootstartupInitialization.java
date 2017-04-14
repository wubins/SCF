/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context.app.cfg;

public interface IBootstartupInitialization {

    /**
     * 初始化资源
     *
     * @param config
     * @throws Exception
     */
    public void init(Object[] config) throws Exception;

    /**
     * 需要载入的配置文件位置
     *
     * @return
     */
    public String[] getResource();

    /**
     * 启动载入顺序
     *
     * @return
     */
    public int sort();
}
