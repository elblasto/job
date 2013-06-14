/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ph.dal.entities.documents;

import java.math.BigDecimal;
import ph.dal.entities.references.Client;
import ph.dal.entities.staff.Employee;
import ph.dal.entities.staff.Position;
import ph.dal.entities.references.ClientDistributAdress;
import java.util.Date;


/**
 * Накладная на выдачу продукции в экспедиции.
 * @author pdkmn
 */
public class DistrInvoice {
    private Integer  id;

    /**
     * Заявка из оперативного плана
     */
    private Order    order;

    /**
     * Получатель продукции
     */
    private Client   client;

    /**
     * Количество копий продукции
     */
    private Integer  copyCount;

    /**
     * Количество копий в стандартной пачке
     */
    private Integer  copiesPerStandartPack;

    /**
     * Количество стандартных пачек
     */
    private Integer  standartPackCount;


    /**
     * Количество копий в нестандартной пачке (остаток)
     */
    private Integer  copiesPerNonStandartPack;

    /**
     * Дата накладной
     */
    private Date     docDate;


    /**
     * Дата готовности к выдаче
     */
    private Date     readyDate;

    /**
     * Дата фактической выдачи
     */
    private Date     shipmentDate;


    /**
     * Сотрудник, выдавший продукцию получателю
     */
    private Employee employee;


    /**
     * Доп. информация о получателе
     */
    private String   clientAddInfo;

    private String   comment;
    /**
     * Перевозчик
     */
    private Transporters transporter;
    /**
     * Машина
     */
    private TransporterCars car;
    /**
     * Водитель
     */
    private TransporterDrivers driver;
    /**
     * Адрес получателя
     */
    private ClientDistributAdress clientAdress;
    
    private BigDecimal weightOneDoc; 
    private Boolean isPayer;
    private Position pos;

    public DistrInvoice() {
    }

    public DistrInvoice(OrderDistrItem distrItem) {
        super();
        this.order = distrItem.getOrder();
        this.client = distrItem.getClient();
        this.copyCount = new Integer(distrItem.getCopyCount().intValue());
        this.copiesPerStandartPack = new Integer(distrItem.getOrder().getCopiesPerPack().intValue());
        this.standartPackCount = this.copyCount.intValue()/this.copiesPerStandartPack.intValue();
        this.copiesPerNonStandartPack = this.copyCount.intValue()%this.copiesPerStandartPack.intValue();
        this.docDate = new Date();
        
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DistrInvoice other = (DistrInvoice) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.order != other.order && (this.order == null || !this.order.equals(other.order))) {
            return false;
        }
        if (this.client != other.client && (this.client == null || !this.client.equals(other.client))) {
            return false;
        }
        if (this.clientAdress != other.clientAdress && (this.clientAdress == null || !this.clientAdress.equals(other.clientAdress))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 43 * hash + (this.order != null ? this.order.hashCode() : 0);
        hash = 43 * hash + (this.client != null ? this.client.hashCode() : 0);
        hash = 43 * hash + (this.clientAdress != null ? this.clientAdress.hashCode() : 0);
        return hash;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getCopyCount() {
        return copyCount;
    }

    public void setCopyCount(Integer copyCount) {
        this.copyCount = copyCount;
    }

    public Integer getCopiesPerStandartPack() {
        return copiesPerStandartPack;
    }

    public void setCopiesPerStandartPack(Integer copiesPerStandartPack) {
        this.copiesPerStandartPack = copiesPerStandartPack;
    }

    public Integer getStandartPackCount() {
        return standartPackCount;
    }

    public void setStandartPackCount(Integer standartPackCount) {
        this.standartPackCount = standartPackCount;
    }

    public Integer getCopiesPerNonStandartPack() {
        return copiesPerNonStandartPack;
    }

    public void setCopiesPerNonStandartPack(Integer copiesPerNonStandartPack) {
        this.copiesPerNonStandartPack = copiesPerNonStandartPack;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public Date getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(Date readyDate) {
        this.readyDate = readyDate;
    }

    public Date getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getClientAddInfo() {
        return clientAddInfo;
    }

    public void setClientAddInfo(String clientAddInfo) {
        this.clientAddInfo = clientAddInfo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public TransporterCars getCar() {
        return car;
    }

    public void setCar(TransporterCars transportercar) {
        this.car = transportercar;
    }
    
    public TransporterDrivers getDriver() {
        return driver;
    }

    public void setDriver(TransporterDrivers transpoterdriver) {
        this.driver = transpoterdriver;
    }
    
    public Transporters getTransporter() {
        return transporter;
    }

    public void setTransporter(Transporters transporter) {
        this.transporter = transporter;
    }
    
    public ClientDistributAdress getClientAdress() {
        return clientAdress;
    }

    public void setClientAdress(ClientDistributAdress clientAdress) {
        this.clientAdress = clientAdress;
    }
    
    public BigDecimal getWeightOneDoc(){
        
    return weightOneDoc;
    };
    
    public void setWeightOneDoc(BigDecimal WeightOneDoc){
    this.weightOneDoc=WeightOneDoc;     
    }
          
    public Boolean getIsPayer(){
    return isPayer;
    };
    
    public void setIsPayer(Boolean isPayer){
    this.isPayer=isPayer;     
    }
    public Position getPos(){
    return pos;
    };
    
    public void setPos(Position pos){
    this.pos=pos;     
    }
}
