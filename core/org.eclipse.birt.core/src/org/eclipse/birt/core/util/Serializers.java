package org.eclipse.birt.core.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;

public class Serializers
{
    //The extension constants
    private static final String EXTENSION_POINT = "org.eclipse.birt.core.SerializationHelper";
    private static final String ELEMENT_HELPER = "Helper";
    private static final String ATTRIBUTE_TYPE = "type";
    private static final String ATTRIBUTE_CLASS = "class";

    private static Map<Integer, ISerializationHelper> helpers;
    
    private static void initHelpers( )
    {
        if ( helpers != null )
            return;

        helpers = new LinkedHashMap<Integer, ISerializationHelper>( );
        
        //Find the extension point.
        IExtensionRegistry extReg = Platform.getExtensionRegistry( );
        IExtensionPoint extPoint = extReg.getExtensionPoint( EXTENSION_POINT );

        if ( extPoint == null )
            return;

        //Fetch all extensions
        IExtension[] exts = extPoint.getExtensions( );
        if ( exts == null )
            return;

        //populate category map as per extension.
        for ( int e = 0; e < exts.length; e++ )
        {
            try
            {
                IConfigurationElement[] configElems = exts[e].getConfigurationElements( );
                if ( configElems == null )
                    continue;

                for ( int i = 0; i < configElems.length; i++ )
                {
                    // for element Factory
                    if ( configElems[i].getName( ).equals( ELEMENT_HELPER ) && configElems[i].getAttribute( ATTRIBUTE_TYPE ) != null && configElems[i].getAttribute( ATTRIBUTE_CLASS ) != null)
                    {
                        int type = Integer.parseInt(configElems[i].getAttribute( ATTRIBUTE_TYPE ));
                        ISerializationHelper factory = (ISerializationHelper) configElems[i].createExecutableExtension( ATTRIBUTE_CLASS );
                        helpers.put(type, factory);
                    }
                }
            }
            catch ( BirtException ex )
            {
            }
        }
    }

    public static Object tryReadObject(DataInputStream dis, int type) throws IOException
    {
        initHelpers();
        ISerializationHelper helper = helpers.get(type);
        if (helper != null)
        {
            return helper.readObject(dis);
        }
        return null;
    }

    public static boolean tryWriteObject(DataOutputStream dos, Object obValue) throws IOException
    {
        initHelpers();
        for (Entry<Integer, ISerializationHelper> entry : helpers.entrySet())
        {
            if (entry.getValue().accept(obValue))
            {
                IOUtil.writeInt( dos, entry.getKey() );
                entry.getValue().writeObject(dos, obValue);
                return true;
            }
        }
        return false;
    }
}
