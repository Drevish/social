# Social

### It's the light core of social network. Here you can create account and change settings, find other users, add them as friends and send or receive messages


<h2> How to run</h2>

<ul>
<li>
Clone source code from github
<pre>
git clone https://github.com/Drevish/social.git
cd social
</pre>
</li>

<li>
Install npm dependencies <br/>
<b>Note:</b> you need to have Node.js installed 
<pre>
npm init -y
npm install
npm run build
</pre>
</li>

<li>
Open <b>application.properties</b> at /src/main/resources<br/>
Update datasource url, username and password with your ones<br/>
For example:
<pre>
spring.datasource.url = jdbc:mysql://localhost:3306
spring.datasource.username = root
spring.datasource.password = root
</pre>
</li>
<li>
Import Maven dependencies 
</li>
<li>
Run <b>SocialApplication</b> at src/main/java/com/drevish/social 
</li>
</ul>

<h3>Application is running! Type localhost:8080 in browser url field and enjoy!</h3>
