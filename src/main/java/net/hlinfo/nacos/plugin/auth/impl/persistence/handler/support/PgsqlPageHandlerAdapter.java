
package net.hlinfo.nacos.plugin.auth.impl.persistence.handler.support;

import net.hlinfo.nacos.plugin.auth.impl.constant.AuthPageConstant;
import net.hlinfo.nacos.plugin.auth.impl.model.OffsetFetchResult;
import net.hlinfo.nacos.plugin.auth.impl.persistence.handler.PageHandlerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * pgsql page handler adapter.
 *
 * @author ylcxy
 */
public class PgsqlPageHandlerAdapter implements PageHandlerAdapter {
    
    @Override
    public boolean supports(String dataSourceType) {
        return "pgsql".equals(dataSourceType.toLowerCase()) || "postgresql".equals(dataSourceType.toLowerCase());
    }
    
    @Override
    public OffsetFetchResult addOffsetAndFetchNext(String fetchSql, Object[] arg, int pageNo, int pageSize) {
        if (!fetchSql.contains(AuthPageConstant.LIMIT)) {
            fetchSql += " " + AuthPageConstant.LIMIT_OFFSET;
            List<Object> newArgsList = new ArrayList<>(Arrays.asList(arg));
            newArgsList.add(pageSize);
            newArgsList.add((pageNo - 1) * pageSize);
            
            Object[] newArgs = newArgsList.toArray(new Object[newArgsList.size()]);
            return new OffsetFetchResult(fetchSql, newArgs);
        }
        
        return new OffsetFetchResult(fetchSql, arg);
    }
    
}
