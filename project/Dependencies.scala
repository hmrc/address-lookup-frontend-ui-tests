import sbt.*

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "org.seleniumhq.selenium" % "selenium-java"          % "4.40.0"     % Test,
    "com.vladsch.flexmark"    % "flexmark-all"           % "0.64.8"     % Test,
    "org.scalatest"          %% "scalatest"              % "3.2.19"     % Test,
    "org.scalatestplus"      %% "selenium-4-21"          % "3.2.19.0"   % Test,
    "org.slf4j"               % "slf4j-simple"           % "1.7.36"     % Test,
    "com.typesafe.play"      %% "play-ahc-ws-standalone" % "2.2.14"     % Test,
    "uk.gov.hmrc"            %% "ui-test-runner"         % "0.45.0"     % Test,
    "org.tpolecat"           %% "doobie-core"            % "1.0.0-RC11" % Test,
    "org.tpolecat"           %% "doobie-postgres"        % "1.0.0-RC11" % Test,
    "org.tpolecat"           %% "doobie-scalatest"       % "1.0.0-RC11" % Test,
    "org.assertj"             % "assertj-core"           % "3.27.3"     % Test,
    "com.typesafe.play"      %% "play-json"              % "2.10.8"     % Test
  )

}
