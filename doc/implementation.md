### 实现文档

#### 使用到第三方库

1. [android-async-http](https://github.com/loopj/android-async-http)
2. [fastjson](https://github.com/alibaba/fastjson)
3. [butterknife](https://github.com/JakeWharton/butterknife)



#### 1. Model类

Model类的设计是根据知乎API返回的JSON格式设计的，Model有以下几个

1. `DailyNews` 类用于表示某一天的新闻列表
2. `Detail` 类用于表示某一条新闻的详细信息
3. `Summary` 类用于表示某条新闻的缩略信息
4. `Theme` 类用于某个新闻主题的详细信息
5. `ThemeList`类是`Theme`类的集合、
6. `ThemeNews`类用于表示某天某个主题的新闻列表
7. `TopStory`类用于表示今日头条新闻中的某一项

#### 2. 数据表

为了实现离线缓存，我们需要把每次获取到的json存储在数据库中，数据采用Android自带的SQLite，数据表结构如下

1. `news_before`表用于存储今天之前看过的新闻列表
2. `news_detail`表用于存储某一条新闻的详细信息
3. `latest_news`表用于存储最新的新闻列表


#### 3. 网络请求

+ 知乎API请求

> 利用`andorid-async-http`第三方库实现的异步http请求，在`NetworkUtil`类中封装了知乎API的请求。<br/>
> 调用网络请求先要通过`JsonResponseHandlerFactory`类中的模板方法获取一个`TextHttpResponseHandler`用于处理返回JSON数据。
> 当获取到JSON数据后，使用第三方库`fastjson`将JSON转换为Java对象并通过Handler放回。

+ 图片下载

> 图片的下载同样是通过`NetworkUtil`类中封装的方法异步http请求。
> 同样下载图片需要通过`ImageResponseHandlerFactory`类的静态方法获取一个`BinaryHttpResponseHandler`用于处理返回的图片二进制流。
> 当获取到图片的二进制流保存成Bitmap，调用传入的接口`OnImageDownloaded`返回Bitmap


#### 4. 数据缓存

+ JSON数据缓存

> 每次发起http请求之前都会在数据库中查询是否有当前需要请求的JSON数据，若有直接从数据库中读取数据放回，若无则通过发起http请求下载数据，下载完成后写入数据(这部分逻辑在`NewsService`类中实现）

+ 图片缓存

> 跟JSON数据一样，每次下载图片之前都会再本地缓存中查找是否有该图片的缓存。我们采用将图片下载URL的MD5值作为图片的索引存储在本地，每次通过查询下载URL的MD5值为文件名的文件是否存在判定是否图片有缓存(这部分逻辑再`ImageProvider`类中实现)

+ 新闻详情缓存

> 因为网页的详情是通过`WebView`渲染的，所以只要通过配置`WebView`的`CacheMode`为`WebSettings.LOAD_CACHE_ELSE_NETWORK`同时将`DomStorageEnable`和`DatabaseEnable`都设置为`true`即可

#### 5. 无图模式
通过限制图片加载以及在WebView中限制图片下载，将webView的`setLoadsImagesAutomatically`和`setBlockNetworkImage`都配置成`false`。

#### 6. 热门新闻中ViewPager+Indicator实现
自定义了一个ViewPagerWithIndicator的View， 内含一个ViewPager和一个LinearLayout，用于存放Indicator。  
核心实现方法为：重写`onPageSelected`函数，动态改变indicator的颜色。  
这个View封装了ViewPager和Adapter，只需要调用`setContent`方法即可完成配置。  
