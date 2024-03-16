# MovieREC

![main](./src/main/resources/static/img/movierec.ico)

<p align="left" display="inline-block">
    <img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white"/>
    <img src="https://img.shields.io/badge/AmazonS3-569A31?style=flat-square&logo=AmazonS3&logoColor=white"/>
    <img src="https://img.shields.io/badge/AmazonRDS-527FFF?style=flat-square&logo=AmazonRDS&logoColor=white"/>
    <img src="https://img.shields.io/badge/PostgreSQL-4479A1?style=flat-square&logo=PostgreSQL&logoColor=white"/>
    <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat-square&logo=Thymeleaf&logoColor=white"/>



## 📃프로젝트 소개

영화 검색 및 정보, 리뷰 작성 서비스를 제공하는 Spring 기반의 웹 어플리케이션입니다. 



### 🔧  Service Architecture

------
<p align="center"><img src="./src/main/resources/static/img/movierec_arch.png" alt="architecture" width="60%" height="60%" /></p>






## 🎞 화면 구성


- 메인 페이지

  <p align="center"><img src="./src/main/resources/static/img/mainpage.png" alt="architecture" width="60%" height="60%" /></p>

- 로그인

  <p align="center"><img src="./src/main/resources/static/img/login.png" alt="choice" width="60%" height="60%" /></p>

- 회원가입

  <p align="center"><img src="./src/main/resources/static/img/signup.png" alt="architecture" width="60%" height="60%" /></p>

- 영화 정보

  <p align="center"><img src="./src/main/resources/static/img/moviedetail.png" alt="architecture" width="60%" height="60%" /></p>

- 리뷰 작성

  <p align="center"><img src="./src/main/resources/static/img/review.png" alt="architecture" width="60%" height="60%" /></p>




## 💻 문제 해결

- InstanceAgetn::Plugins::CodeDeployPlugin::Commandpoller: Calling putHostCommandComplete: "Code Error"
  - yml 파일이 gitignore 에 설정 되어있어 파일이 안올라갔던 문제로 gitignore 수정하여 해결


- Github Actions 을 통해 진행하는 CD 프로세스 중 빌드하는 과정에서 jar 파일이 2개 생성되는 현상
  - build.gradle 파일에 jar { enabled = false } 코드를 추가하여 plain.jar 파일이 생성되지 않게 함
<p align="center"><img src="./src/main/resources/static/img/ga.png" alt="architecture" width="60%" height="60%" /></p>
  
