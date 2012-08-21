package org.eclipse.birt.core.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface ISerializationHelper
{
    boolean accept(Object obj);
    void writeObject( DataOutputStream dos, Object obValue ) throws IOException;
    Object readObject( DataInputStream dis ) throws IOException;
}
