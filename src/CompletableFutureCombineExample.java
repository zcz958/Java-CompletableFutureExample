import java.util.concurrent.CompletableFuture;

public class CompletableFutureCombineExample {

  public static class UserInfo {
    private int userId;
    private UserName name;
    private UserPhone phone;

    public void setUserId(int userId) {
      this.userId = userId;
    }

    public void setName(UserName name) {
      this.name = name;
    }

    public void setPhone(UserPhone phone) {
      this.phone = phone;
    }

    @Override
    public String toString() {
      return String.format("User info: id = %d  Name: %s  Phone: %s", userId, name.getName(), phone.getPhone());
    }
  }

  public static class UserName {
    private String name;

    public void setName(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  public static class UserPhone {
    private String phone;

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getPhone() {
      return phone;
    }
  }

  public static void main(String[] args) throws Exception {

    CompletableFuture<UserName> userName = CompletableFuture.supplyAsync(() ->
      {
        System.out.println("Creating userName");
        return new UserName();
      }
    ).thenComposeAsync((UserName name) -> CompletableFuture.supplyAsync(() ->
      {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          System.out.println(e.getMessage());
        }

        System.out.println("Populating name");
        name.setName("Bill");
        return name;
      }
    ));

    CompletableFuture<UserPhone> userPhone = CompletableFuture.supplyAsync(() ->
      {
        System.out.println("Creating phone");
        return new UserPhone();
      }
    ).thenComposeAsync((UserPhone phone) -> CompletableFuture.supplyAsync(() ->
      {
        System.out.println("Populating phone");
        phone.setPhone("800-000-1234");
        return phone;
      }
    ));

    CompletableFuture<UserInfo> userInfo = userName.thenCombine(userPhone, (UserName name, UserPhone phone) ->
      {
        System.out.println("Combine user and phone");
        UserInfo user = new UserInfo();
        user.setUserId(1);
        user.setName(name);
        user.setPhone(phone);
        return user;
      }
    );

    UserInfo result = userInfo.get();
    System.out.println("Done: " + result.toString());
  }
}