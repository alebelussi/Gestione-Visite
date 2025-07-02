package program.places;

public interface PlaceView {

    String askPlaceName();
    String askPlaceDescription();
    String askPlaceAddress();
    String askRegion();
    int askMenuVoice();
    void showPlace(Place place);
    void showAlreadyInsertedError();
    void showPlaceNotFoundError();
    void showInsertSuccess();
    void showModifySuccess();
    void showCancelModify();
    void showRemoveSuccess();
    void showCancelRemove();
    void showNoPlaceAvailable();
    void showNoPlace();
    boolean confirmInsert();
    boolean confirmInsertRegion();
    String confirmModify();
    boolean confirmRemove();
}