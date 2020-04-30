import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AppendingObjectOutputStream extends ObjectOutputStream {
    public AppendingObjectOutputStream(OutputStream out) throws IOException {
      super(out);
    }
  
    @Override
    protected void writeStreamHeader() throws IOException {
      reset();
    }
}