# 各种自定义 View. 
## 一、图表. 
### 1、3D立体饼图. 
  3D立体的饼图，可自定义饼图的开始角度。  
  从内到外的原则，依次画底部椭圆，背部立面，内部矩形，外部立面，顶部椭圆，顶部滑过的扇形.
   <div align=center><img width="150" height="150" src="https://github.com/wongtaian/CustomView/blob/master/png/BE29CEE797E6D644D00AE77A557184E7.png"/></div>.


 ### 2、贝塞尔曲线和坐标
  多组数据的三阶贝塞尔曲线，曲线穿过所有的数据点，并计算出曲线上的每个点的坐标.
 <div align=center><img width="300" height="200" src="https://github.com/wongtaian/CustomView/blob/master/png/bezierViw.gif"/></div>.

 ### 3、柱状图.
   简单的柱状图，就不需要引用第三方的框架了。算好位置，用 canvas.drawRect(left, top, right, bottom, paint) 方法画几个矩形就可以了。
   <div align=center><img width="300" height="300" src="https://github.com/wongtaian/CustomView/blob/master/png/D59CD05159CE687CEB14EF3E78F75A4F.png"/></div>.