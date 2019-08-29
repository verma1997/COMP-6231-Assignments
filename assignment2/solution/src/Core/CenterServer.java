package Core;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

/**
 * The implementation of CenterInterface
 */
public class CenterServer {
    public static void main(String argv[]){
        try {
            ORB orb = ORB.init(new String[]{"-ORBInitialPort",argv[2],"-ORBInitialHost",argv[1]},null);
            int runMode = Integer.parseInt(argv[0]);
            if (runMode == 0){	//create three server on the same machine
                CenterServant.hostDDO = "localhost";
                CenterServant.hostMTL = "localhost";
                CenterServant.hostLVL = "localhost";
                CenterServant MTL = new CenterServant("MTL",argv[1],argv[2]);
                CenterServant LVL = new CenterServant("LVL",argv[1],argv[2]);
                CenterServant DDO = new CenterServant("DDO",argv[1],argv[2]);


                orb.connect(MTL);

                org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
                NamingContext ncRef = NamingContextHelper.narrow(objRef);

                NameComponent nc = new NameComponent("MTL","");
                NameComponent nc2 = new NameComponent("LVL","");
                NameComponent nc3 = new NameComponent("DDO","");
                NameComponent path[] = {nc};

                ncRef.rebind(path, MTL);

                orb.connect(LVL);
                path = new NameComponent[] {nc2};
                ncRef.rebind(path, LVL);

                orb.connect(DDO);
                path = new NameComponent[] {nc3};
                ncRef.rebind(path, DDO);
                System.out.println("Three servers are up and running, registered to name service on "+argv[1]+":"+argv[2]+"!");
            }
            else {  //create server individually
                CenterServant singleServer = new CenterServant(argv[3],argv[1],argv[2]);
                orb.connect(singleServer);

                org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
                NamingContext ncRef = NamingContextHelper.narrow(objRef);

                NameComponent nc = new NameComponent(argv[3],"");
                NameComponent path[] = {nc};

                ncRef.rebind(path, singleServer);
                System.out.println(argv[3]+" server is up and running, registered to name service on "+argv[1]+":"+argv[2]+"!");
            }
            java.lang.Object sync = new java.lang.Object();
            synchronized(sync) {
                sync.wait();
            }
        }catch (Exception e){
            System.err.println("ERROR:"+e);
            e.printStackTrace(System.out);
        }
    }
}
