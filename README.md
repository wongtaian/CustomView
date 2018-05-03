# 各种自定义 View. 
## 一、图表. 
### 1、[3D立体饼图](https://github.com/wongtaian/CustomView/blob/ca132770ef027e3c26d0bab3f2a2b9e170eab2b5/app/src/main/java/com/konggeek/hz/customview/chart/PieChart3DView.java).
  3D立体的饼图，可自定义饼图的开始角度。
  从内到外的原则，依次画底部椭圆，背部立面，内部矩形，外部立面，顶部椭圆，顶部滑过的扇形.
   <div align=center><img width="150" height="150" src="https://github.com/wongtaian/CustomView/blob/master/png/BE29CEE797E6D644D00AE77A557184E7.png"/></div>.


 ### 2、[贝塞尔曲线和坐标](https://github.com/wongtaian/CustomView/blob/ca132770ef027e3c26d0bab3f2a2b9e170eab2b5/app/src/main/java/com/konggeek/hz/customview/chart/BezierViw.java)
  多组数据的三阶贝塞尔曲线，曲线穿过所有的数据点，并计算出曲线上的每个点的坐标.
 <div align=center><img width="300" height="200" src="https://github.com/wongtaian/CustomView/blob/master/png/bezierViw.gif"/></div>.

 ### 3、[柱状图](https://github.com/wongtaian/CustomView/blob/ca132770ef027e3c26d0bab3f2a2b9e170eab2b5/app/src/main/java/com/konggeek/hz/customview/chart/BarChartView.java).
   简单的柱状图，就不需要引用第三方的框架了。算好位置，用 canvas.drawRect(left, top, right, bottom, paint) 方法画几个矩形就可以了。
   <div align=center><img width="300" height="300" src="https://github.com/wongtaian/CustomView/blob/master/png/D59CD05159CE687CEB14EF3E78F75A4F.png"/></div>.

### 4、[时钟](https://github.com/wongtaian/CustomView/blob/ca132770ef027e3c26d0bab3f2a2b9e170eab2b5/app/src/main/java/com/konggeek/hz/customview/chart/ClockView.java)
   模拟时钟，主要是 canvas 的旋转和移动。绘制文字时，不能再用简单的旋转，否则文字不是正向的。用三角函数的话精确度不够。如下图绿点是刻度的位置，红点是三角函数计算出来的位置。
   <div align=center><img width="300" height="300" src="https://github.com/wongtaian/CustomView/blob/master/png/clock1.png"/></div>.

   这里我采用了旋转加位移，绘制文字后再原路返回，重复12次绘制，如下图，红绿点重合了。
   <div align=center><img width="300" height="300" src="https://github.com/wongtaian/CustomView/blob/ca132770ef027e3c26d0bab3f2a2b9e170eab2b5/png/clock2.jpg"/></div>.

   绘制文字还有问题时，canvas.drawText 是从大概文字的左下角做为原点的。这里我用 paint.getTextBounds 的方法获取文字的占用范围，是个矩形，把文字的绘制原点向左挪动矩形宽的一半，向下移动矩形高的一半，这样绘制出的文字的中心点正好与刻度重合。如图，红色方块就是文字没挪动之前的位置。
   <div align=center><img width="300" height="300" src="https://github.com/wongtaian/CustomView/blob/ca132770ef027e3c26d0bab3f2a2b9e170eab2b5/png/clock3.jpg"/></div>

### 5、[雷达图](https://github.com/wongtaian/CustomView/blob/91c32973bc3cbf9a5c91cc92f953a91cccf9c08d/app/src/main/java/com/konggeek/hz/customview/chart/RadarView.java)
仿英雄联盟能力值的雷达图。
<div align=center><img width="300" height="300" src="https://github.com/wongtaian/CustomView/blob/91c32973bc3cbf9a5c91cc92f953a91cccf9c08d/png/radar.png"/></div>

  ## 二、ViewGroup
### 1、[StarBar-评价](https://github.com/wongtaian/CustomView/blob/master/app/src/main/java/com/konggeek/hz/customview/vg/StarBar.java)
系统自带的 RatingBar 在设置星星大小，间距时不是很方便，效果和 UI 上也总是有差距。所以做了个自定义的 StarBar，继承自 LinearLayout ，并添加 ImageView 做为星星，根据点击位置设置点亮星星的个数。  <div align=center><img width="300" height="300" src="https://github.com/wongtaian/CustomView/blob/dc8fb6f54f499f78b8aaf8d6d32dbb23cb1a52f7/png/1017788CD4B230110F1F1C04BF39B107.png"/></div>
    可设置的属性：
      <declare-styleable name="StarBar">
        <!--星星总数-->
        <attr name="numStar" format="integer"/>
        <!--步进值-->
        <attr name="stepHalf" format="boolean"/>
        <!--点亮星星数-->
        <attr name="value" format="float"/>
        <!--点亮星星的图片资源-->
        <attr name="progressRsdId" format="reference"/>
        <!--亮一半的图片资源-->
        <attr name="secondProgressResId" format="reference"/>
        <!--没亮的图片资源-->
        <attr name="normalResId" format="reference"/>
        <!--星星之间的距离-->
        <attr name="marginLeftStar" format="dimension"/>
        <attr name="marginRightStar" format="dimension"/>
         <!--星星的宽度-->
        <attr name="widthStar" format="dimension"/>
        <!--星星的高度-->
        <attr name="heightStar" format="dimension"/>
        <!--是否是指示器，true 不允许用户修改 -->
        <attr name="isIndicator" format="boolean"/>
    </declare-styleable>