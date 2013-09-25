package articles.web.requests;



public abstract class ResourceRequest<T, E> extends BusinessRequest {
	protected T dto;
	
	public ResourceRequest(T dto) {
		this.dto = dto;
	}
	
	@Override
	protected Object doProcess() {
		E entity =  toEntity(this.dto);
		return processEntity(entity);
	}
	
	protected abstract E toEntity(T dto);
	protected abstract Object  processEntity(E entity);
}
