/*
 * Copyright 1999-2022 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.hlinfo.nacos.plugin.auth.impl.authenticate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.ObjectProvider;

import com.alibaba.nacos.auth.config.AuthConfigs;
import com.alibaba.nacos.plugin.auth.api.Permission;
import com.alibaba.nacos.plugin.auth.exception.AccessException;

import net.hlinfo.nacos.plugin.auth.impl.constant.AuthSystemTypes;
import net.hlinfo.nacos.plugin.auth.impl.users.NacosUser;

/**
 * Authentication Proxy.
 *
 * @author Weizhan▪Yun
 * @date 2023/1/12 23:31
 */
public class AuthenticationManagerDelegator implements IAuthenticationManager {
    
    private ObjectProvider<DefaultAuthenticationManager> defaultAuthenticationManager;
    
    private ObjectProvider<LdapAuthenticationManager> ldapAuthenticationManager;
    
    private AuthConfigs authConfigs;
    
    public AuthenticationManagerDelegator(ObjectProvider<DefaultAuthenticationManager> nacosAuthManager,
                                          ObjectProvider<LdapAuthenticationManager> ldapAuthenticationProvider, AuthConfigs authConfigs) {
        this.defaultAuthenticationManager = nacosAuthManager;
        this.ldapAuthenticationManager = ldapAuthenticationProvider;
        this.authConfigs = authConfigs;
    }
    
    @Override
    public NacosUser authenticate(String username, String password) throws AccessException {
        return getManager().authenticate(username, password);
    }
    
    @Override
    public NacosUser authenticate(String jwtToken) throws AccessException {
        return getManager().authenticate(jwtToken);
    }
    
    @Override
    public NacosUser authenticate(HttpServletRequest httpServletRequest) throws AccessException {
        return getManager().authenticate(httpServletRequest);
    }
    
    @Override
    public void authorize(Permission permission, NacosUser nacosUser) throws AccessException {
        getManager().authorize(permission, nacosUser);
    }
    
    @Override
    public boolean hasGlobalAdminRole(String username) {
        return getManager().hasGlobalAdminRole(username);
    }
    
    @Override
    public boolean hasGlobalAdminRole(NacosUser nacosUser) {
        return getManager().hasGlobalAdminRole(nacosUser);
    }
    
    private IAuthenticationManager getManager() {
        if (AuthSystemTypes.LDAP.name().equalsIgnoreCase(authConfigs.getNacosAuthSystemType())) {
            return ldapAuthenticationManager.getIfAvailable();
        }
        
        return defaultAuthenticationManager.getIfAvailable();
    }
}