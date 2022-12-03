package dungeonmania.main.entities.moving;

// interface for subject (the player) in observer i.e ally relationship
public interface SubjectPlayer {

	public void attachAlly(ObserverPlayer ally);
	public void detachAlly(ObserverPlayer ally);
	public void notifyObserversOfMovement();
	public void observerTickUpdates();

}
