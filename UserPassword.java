import java.util.StringTokenizer;

class UserPassword {
  
  String nickname;
  String username;
  String password;
  String url;
  
  UserPassword(String s) {
    
    SeperateText(s);
  }
  
  UserPassword() {
    
  }
  
  public boolean equals(Object obj) {
    
    return nickname.equalsIgnoreCase(((UserPassword)obj).nickname);
  }
  
  private void SeperateText(String s) {
    
    StringTokenizer tokens = new StringTokenizer(s, ";");
    
    if (tokens.hasMoreTokens()) nickname = tokens.nextToken();
    
    if (tokens.hasMoreTokens()) username = tokens.nextToken();
    
    if (tokens.hasMoreTokens()) password = tokens.nextToken();

    if (tokens.hasMoreTokens()) url = tokens.nextToken();
  }
}