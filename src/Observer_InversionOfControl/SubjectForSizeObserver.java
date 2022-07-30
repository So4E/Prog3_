package Observer_InversionOfControl;

public interface SubjectForSizeObserver {
    void meldeAn(Observer observer);
    void meldeAb(Observer observer);
    void benachrichtige();
}
