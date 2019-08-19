package com.mndeveci;

public class PersonRequestResponseWrapper {

  private PersonRequest request;
  private PersonResponse response;

  public PersonRequestResponseWrapper(PersonRequest request, PersonResponse response) {
    this.request = request;
    this.response = response;
  }

  public PersonRequestResponseWrapper() {
  }

  public PersonRequest getRequest() {
    return request;
  }
  public void setRequest(PersonRequest request) {
    this.request = request;
  }
  public PersonResponse getResponse() {
    return response;
  }
  public void setResponse(PersonResponse response) {
    this.response = response;
  }
}
