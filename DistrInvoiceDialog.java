
package ph.client.distr;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import ph.client.commons.baseui.AbstractActionHandler;
import ph.client.commons.baseui.ActionFactory;
import ph.client.commons.baseui.DataAwareDialog;
import ph.client.commons.baseui.ObjectLabel;
import ph.client.commons.baseui.ObjectTextField;
import ph.client.commons.binding.BindingException;
import ph.client.commons.binding.IntToStrConverter;
import ph.client.commons.binding.Validator;
import ph.client.commons.exceptions.ExceptionDialog;
import ph.dal.dao.DAOFactory;
import ph.dal.entities.documents.DistrInvoice;
import ph.dal.entities.references.Client;
import ph.dal.entities.staff.Department;
import ph.dal.entities.staff.Employee;
import ph.dal.entities.references.ClientDistributAdress;
import ph.dal.entities.documents.Transporters;
import ph.dal.entities.documents.TransporterCars;
import ph.dal.entities.documents.TransporterDrivers;

public class DistrInvoiceDialog extends DataAwareDialog {
    private DistrInvoice invoice;
    private Client clhelp;
    private Transporters trhelp;
    private ObjectTextField clientField;
    private ObjectTextField clientAdressField;
    private ObjectTextField transporterField;
    private ObjectTextField carField;
    private ObjectTextField driverField;
    private JTextField copyCountField;
    private JTextField copiesPerStandartPackField;
    private JTextField standartPackCountField;
    private JTextField copiesPerNonStandartPackField;
    private JDateChooser readyDateField;
    private JDateChooser shipmentDateField;
    private ObjectTextField employeeField;
    private Action chooseClient;
    private Action chooseClientAdress;
    private Action chooseTransporter;
    private Action chooseCar;
    private Action chooseDriver;
    private Action chooseEmployee;
    private AbstractActionHandler actionHandler;
    private ObjectLabel docDate;
    private ObjectLabel orderNum;
    private ObjectLabel product;
    private JTextField  addInfoField;
    private JTextArea   commentArea;


    public DistrInvoiceDialog(DAOFactory dao, DistrInvoice invoice) {
        setDao(dao);
        this.invoice = invoice;
    }

    private void chooseClientActionPerformed(){
        Client cl = ChooseClientDialog.getClient(getDao(), invoice.getClient());
        clhelp=cl;
        if (cl!=null)
           clientField.setObject(cl); 
            }

    private void chooseClientAdressActionPerformed(Client cl){
        if (cl==null){
        cl=invoice.getClient();
        }
        ClientDistributAdress cladress = ChooseClientAderssDialog.getClientAdress(getDao(), invoice.getClientAdress(),cl);
        if (cladress!=null)
            clientAdressField.setObject(cladress);       
    }
    
    
    private void chooseTransporterActionPerformed(){
        Transporters trans = ChooseTransporterDialog.getTransporter(getDao(), invoice.getTransporter());
        trhelp=trans;
        if (trans!=null)
            transporterField.setObject(trans);       
    }
    
    private void chooseCarActionPerformed(Transporters tr){
        if (tr==null){
            tr=invoice.getTransporter();
        }
        TransporterCars trcar = ChooseCarDialog.getTransporterCar(getDao(), invoice.getCar(),tr);
        if (trcar!=null)
            carField.setObject(trcar);       
    } 
    
    private void chooseDriverActionPerformed(Transporters tr){
        if (tr==null){
            tr=invoice.getTransporter();
        }
        TransporterDrivers trdriv = ChooseDriverDialog.getTransporterDriver(getDao(), invoice.getDriver(),tr);
        if (trdriv!=null)
            driverField.setObject(trdriv);       
    } 
    private void chooseEmployeeActionPerformed(){
        Employee emp = SelEmpDialog.select(getDao(),(Employee) employeeField.getObject(), new Department(11));//ChooseEmpDialog.getEmployee(getDao(),new Department(11),(Employee) employeeField.getObject());
        if (emp!=null)
            employeeField.setObject(emp);
    }


    @Override
    protected void doConfirmAction() throws Exception {
//    getDao().getDistrDAO().saveInvoice(invoice);
    }

    @Override
    protected void createComponents() {
        clientField = new ObjectTextField();
        clientAdressField = new ObjectTextField();
        transporterField = new ObjectTextField();
        carField = new ObjectTextField();
        driverField = new ObjectTextField();
        copyCountField = new JTextField(6);
        copiesPerStandartPackField = new JTextField(6);
        standartPackCountField = new JTextField(6);
        copiesPerNonStandartPackField = new JTextField(6);
        readyDateField = new JDateChooser("dd.MM.yyyy HH:mm", "##.##.#### ##:##", '_');
        shipmentDateField = new JDateChooser("dd.MM.yyyy HH:mm", "##.##.#### ##:##", '_');
        employeeField = new ObjectTextField();
        docDate = new ObjectLabel("dd.MM.yyyy");
        orderNum = new ObjectLabel();
        product  = new ObjectLabel();
        addInfoField = new JTextField();
        commentArea = new JTextArea();

        actionHandler = new ActionHandler();
        chooseClient = ActionFactory.createAction("...", "Выбрать получателя", actionHandler);
        chooseClientAdress = ActionFactory.createAction("...", "Выбрать адрес получателя", actionHandler);
        chooseTransporter = ActionFactory.createAction("...", "Выбрать перевозчика", actionHandler);
        chooseCar = ActionFactory.createAction("...", "Выбрать машину", actionHandler);
        chooseDriver = ActionFactory.createAction("...", "Выбрать водителя", actionHandler);
        chooseEmployee = ActionFactory.createAction("...", "Выбрать сотрудника", actionHandler);
    }

    @Override
    protected void initComponents() {
        clientField.setEditable(false);
        clientAdressField.setEditable(false);
        transporterField.setEditable(false);
        carField.setEditable(false);
        driverField.setEditable(false);
        employeeField.setEditable(false);

    }

    @Override
    protected void bindComponents() throws BindingException {
        getBindingList().addBinding(invoice, "docDate", docDate, "object").setReadOnly();
        getBindingList().addBinding(invoice, "order.orderNum", orderNum, "object").setReadOnly();
        getBindingList().addBinding(invoice, "client", clientField, "object");
        getBindingList().addBinding(invoice, "clientAdress", clientAdressField, "object");
        getBindingList().addBinding(invoice, "transporter", transporterField, "object");
        getBindingList().addBinding(invoice, "car", carField, "object");
        getBindingList().addBinding(invoice, "driver", driverField, "object");
        getBindingList().addBinding(invoice, "clientAddInfo", addInfoField, "text");
        getBindingList().addBinding(invoice, "comment", commentArea, "text");
        getBindingList().addBinding(invoice, "employee", employeeField, "object");
        getBindingList().addBinding(invoice, "order.product.name", product, "text").setReadOnly();
        getBindingList().addBinding(invoice, "copyCount", copyCountField, "text").setConverter(new IntToStrConverter()).setValidator(new IntFieldValidator());
        getBindingList().addBinding(invoice, "copiesPerStandartPack", copiesPerStandartPackField, "text").setConverter(new IntToStrConverter());
        getBindingList().addBinding(invoice, "standartPackCount", standartPackCountField, "text").setConverter(new IntToStrConverter());
        getBindingList().addBinding(invoice, "copiesPerNonStandartPack", copiesPerNonStandartPackField, "text").setConverter(new IntToStrConverter());
        getBindingList().addBinding(invoice, "readyDate", readyDateField, "date");
        getBindingList().addBinding(invoice, "shipmentDate", shipmentDateField, "date");
        getBindingList().bindForward();
        if (invoice.getId() == null)
            employeeField.setObject(getDao().getUserContext().getEmployee());
    }

    @Override
    protected void layoutComponents() {
        JPanel clientPanel = new JPanel(new BorderLayout());
        clientPanel.add(clientField, BorderLayout.CENTER);
        clientPanel.add(new JButton(chooseClient),BorderLayout.EAST);
        
        JPanel clientAdressPanel = new JPanel(new BorderLayout());
        clientAdressPanel.add(clientAdressField, BorderLayout.CENTER);
        clientAdressPanel.add(new JButton(chooseClientAdress),BorderLayout.EAST);
        
        JPanel transporterPanel = new JPanel(new BorderLayout());
        transporterPanel.add(transporterField, BorderLayout.CENTER);
        transporterPanel.add(new JButton(chooseTransporter),BorderLayout.EAST);
        
        JPanel transportercarPanel = new JPanel(new BorderLayout());
        transportercarPanel.add(carField, BorderLayout.CENTER);
        transportercarPanel.add(new JButton(chooseCar),BorderLayout.EAST);
        
        JPanel transporterdriverPanel = new JPanel(new BorderLayout());
        transporterdriverPanel.add(driverField, BorderLayout.CENTER);
        transporterdriverPanel.add(new JButton(chooseDriver),BorderLayout.EAST);
        
        JPanel empPanel = new JPanel(new BorderLayout());
        empPanel.add(employeeField, BorderLayout.CENTER);
        empPanel.add(new JButton(chooseEmployee),BorderLayout.EAST);

        FormLayout layout = new FormLayout("pref, 3dlu, pref, 3dlu, pref, 3dlu, pref",
                                           "pref, 10dlu, pref, 3dlu,pref, 3dlu,pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref,3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, fill:20dlu, 10dlu, pref,");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.setDefaultDialogBorder();
        builder.addLabel("Накладная на отпуск продукции",cc.xyw(1,1,7)).setHorizontalAlignment(JLabel.CENTER);
        builder.addLabel("Дата составления:",cc.xy(1,3));
        builder.addLabel("№ заявки:",cc.xy(1,5));
        builder.addLabel("Издание:",cc.xy(1,7));
        builder.addLabel("Сдача груза:",cc.xy(1,9));
        builder.addLabel("Адрес получателя:",cc.xy(1,11));
        builder.addLabel("Перевозчик:",cc.xy(1,13));
        builder.addLabel("Машина:",cc.xy(1,15));
        builder.addLabel("Водитель:",cc.xy(1,17));
        builder.addLabel("Доп. информация:",cc.xy(1,19));
        builder.addLabel("Тираж:",cc.xy(1,21));
        builder.addLabel("Кол-во в стандартной пачке:",cc.xy(5,21));
        builder.addLabel("Кол-во стандартных пачек:",cc.xy(1,23));
        builder.addLabel("Кол-во в нестандартной пачке:",cc.xy(5,23));
        builder.addLabel("Особые отметки:",cc.xy(1,25));



        builder.add(docDate,cc.xy(3, 3));
        builder.add(orderNum,cc.xy(3, 5));
        builder.add(product,cc.xyw(3, 7,5));
        builder.add(clientPanel,cc.xyw(3,9,5));
        builder.add(clientAdressPanel,cc.xyw(3,11,5));
        builder.add(transporterPanel,cc.xyw(3,13,5));
        builder.add(transportercarPanel,cc.xyw(3,15,5));
        builder.add(transporterdriverPanel,cc.xyw(3,17,5));
        builder.add(addInfoField,cc.xyw(3,19,5));
        builder.add(copyCountField,cc.xy(3, 21));
        builder.add(copiesPerStandartPackField,cc.xy(7, 21));
        builder.add(standartPackCountField,cc.xy(3, 23));
        builder.add(copiesPerNonStandartPackField,cc.xy(7, 23));
        builder.add(new JScrollPane(commentArea),cc.xyw(3,25,5));

        FormLayout layout1 = new FormLayout("pref,3dlu,pref,pref:grow","pref,3dlu,pref,3dlu,pref");
        PanelBuilder builder1 = new PanelBuilder(layout1);
        builder1.addLabel("Тираж готов к выдаче:",cc.xy(1,1));
        builder1.addLabel("Время отпуска:",cc.xy(1,3));
        builder1.addLabel("Отпустил:",cc.xy(1,5));
        builder1.add(readyDateField,cc.xy(3, 1));
        builder1.add(shipmentDateField,cc.xy(3, 3));
        builder1.add(empPanel,cc.xyw(3,5,2));

        builder.add(builder1.getPanel(),cc.xyw(1,27,7));


        getMainPanel().setLayout(new BorderLayout());
        getMainPanel().add(builder.getPanel());

    }

    class ActionHandler extends AbstractActionHandler{

        public void actionPerformed(Action action) {
            if (action.equals(chooseClient))
                chooseClientActionPerformed();
            if (action.equals(chooseClientAdress))
                chooseClientAdressActionPerformed(clhelp);
            if (action.equals(chooseTransporter))
                chooseTransporterActionPerformed();
            if (action.equals(chooseCar))
                chooseCarActionPerformed(trhelp);
            if (action.equals(chooseDriver))
                chooseDriverActionPerformed(trhelp);
            if (action.equals(chooseEmployee))
                chooseEmployeeActionPerformed();

        }
    }

    class IntFieldValidator<T> implements Validator<String>{

        public boolean validate(String value, String description) {
            try{
                int copyCount = checkInt(copyCountField.getText(),"Тираж");
                int copiesPerStandartPack = checkInt(copiesPerStandartPackField.getText(),"Кол-во в стандартной пачке");
                int standartPackCount = checkInt(standartPackCountField.getText(),"Кол-во стандартных пачек");
                int copiesPerNonStandartPack = checkInt(copiesPerNonStandartPackField.getText(),"Кол-во в нестандартной пачке");
                checkCounts(copyCount, copiesPerStandartPack, standartPackCount, copiesPerNonStandartPack);
                return true;
            } catch(Exception ex){
                ExceptionDialog.showError(ex, ex.getMessage());
                return false;
            }
        }

        private int checkInt(String value, String description) throws Exception{
            try{
                return Integer.valueOf(value);
            }catch(NumberFormatException ex){
                throw new Exception("Неверно задано заначение в поле \""+description+"\"");
            }
        }

        private void checkCounts(int copyCount, int copiesPerStandartPack, int standartPackCount, int copiesPerNonStandartPack) throws Exception{
            if (copyCount!=copiesPerStandartPack*standartPackCount+copiesPerNonStandartPack)
                throw new Exception("Тираж неверно распределен по пачкам!");
        }
    }
}
