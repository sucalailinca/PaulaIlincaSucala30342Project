package project3;

import Components.Activation;
import Components.Condition;
import Components.GuardMapping;
import Components.PetriNet;
import Components.PetriNetWindow;
import Components.PetriTransition;
import DataObjects.DataCar;
import DataObjects.DataCarQueue;
import DataObjects.DataString;
import DataObjects.DataTransfer;
import DataOnly.TransferOperation;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;

public class Exercise1 {
    public static void main(String[] args) {

        //-------------------------------Lane1--------------------------------

        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Modified Lane Model ";
        pn.NetworkPort = 1090;

        DataCar p1 = new DataCar();
        p1.SetName("P_a");
        pn.PlaceList.add(p1);

        DataCarQueue p2 = new DataCarQueue();
        p2.Value.Size = 3;
        p2.SetName("P_x");
        pn.PlaceList.add(p2);

        DataString p3 = new DataString();
        p3.SetName("P_TL");
        pn.PlaceList.add(p3);

        DataCar p4 = new DataCar();
        p4.SetName("P_b");
        pn.PlaceList.add(p4);


        DataTransfer OP1 = new DataTransfer();
        OP1.SetName("OP");
        OP1.Value = new TransferOperation("localhost", "1091", "in");
        pn.PlaceList.add(OP1);

        DataString full = new DataString();
        full.SetName("full");
        full.SetValue("full");
        pn.ConstantPlaceList.add(full);


        DataString green= new DataString();
        green.SetName("green");
        green.SetValue("green");
        green.Printable= false;
        pn.ConstantPlaceList.add(green);

        // tu ------------------------------------------------
        PetriTransition tu = new PetriTransition(pn);
        tu.TransitionName = "T_u";
        tu.InputPlaceName.add("P_a");
        tu.InputPlaceName.add("P_x");

        Condition tuCtu = new Condition(tu, "P_a", TransitionCondition.NotNull);
        Condition tuCte = new Condition(tu, "P_x", TransitionCondition.CanAddCars);
        tuCtu.SetNextCondition(LogicConnector.AND, tuCte);

        GuardMapping grdtu = new GuardMapping();
        grdtu.condition= tuCtu;
        grdtu.Activations.add(new Activation(tu, "P_a", TransitionOperation.AddElement, "P_x"));
        tu.GuardMappingList.add(grdtu);

        Condition tuCt3 = new Condition(tu, "P_a", TransitionCondition.NotNull);
        Condition tuCt4 = new Condition(tu, "P_x", TransitionCondition.CanNotAddCars);
        tuCt3.SetNextCondition(LogicConnector.AND, tuCt4);

        GuardMapping grdtu1 = new GuardMapping();
        grdtu1.condition= tuCt3;
        grdtu1.Activations.add(new Activation(tu, "full", TransitionOperation.SendOverNetwork, "OP"));
        grdtu1.Activations.add(new Activation(tu, "P_a", TransitionOperation.Copy, "P_a"));
        tu.GuardMappingList.add(grdtu1);

        tu.Delay = 0;
        pn.Transitions.add(tu);


        // te ------------------------------------------------
        PetriTransition te = new PetriTransition(pn);
        te.TransitionName = "T_e";
        te.InputPlaceName.add("P_x");
        te.InputPlaceName.add("P_TL");


        Condition teCtu = new Condition(te, "P_TL", TransitionCondition.Equal,"green");
        Condition teCte = new Condition(te, "P_x", TransitionCondition.HaveCar);
        teCtu.SetNextCondition(LogicConnector.AND, teCte);

        GuardMapping grdte = new GuardMapping();
        grdte.condition= teCtu;
        grdte.Activations.add(new Activation(te, "P_x", TransitionOperation.PopElementWithoutTarget, "P_b"));
        grdte.Activations.add(new Activation(te, "P_TL", TransitionOperation.Move, "P_TL"));

        te.GuardMappingList.add(grdte);

        te.Delay = 0;
        pn.Transitions.add(te);


        //----------------------------PN Start-------------------------------------------------

        System.out.println("Exercise1 started \n ------------------------------");
        pn.Delay = 1800;
        //pn.Start();

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}