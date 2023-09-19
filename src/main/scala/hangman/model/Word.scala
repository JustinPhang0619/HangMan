package hangman.model

import hangman.util.Database
import scalafx.beans.property.StringProperty
import scalikejdbc._

import scala.util.Try


class Word(val word: String) extends Database {
  def this() = this(null)

  var wordProperty = new StringProperty(word)

  def isExist: Boolean = {
    DB readOnly { implicit session =>
      sql"""
        select * from Word where word = ${wordProperty.value}
      """.map(rs => rs.string("word")).single.apply()
    } match {
      case Some(x) => true
      case None => false
    }
  }

  def save(): Try[Int] = {
    if (!isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
          insert into Word (word) values
            (${wordProperty.value})
        """.update.apply()
      })
    } else {
      Try(DB autoCommit { implicit session =>
        sql"""
        update Word
        set
        word  = ${wordProperty.value}
         where word = ${wordProperty.value}
        """.update.apply()
      })
    }

  }

  def delete(): Try[Int] = {
    if (isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
        delete from Word where
          word = ${wordProperty.value}
        """.update.apply()
      })
    } else
      throw new Exception("Word not Exists in Database")
  }

}

object Word extends Database {
  def apply(wordS : String): Word =
    new Word(wordS) {
      wordProperty = new StringProperty(wordS)
    }

  def initializeTable() = {
    DB autoCommit { implicit session =>
      sql"""
        create table Word (
          word varchar(256) not null primary key
        )
      """.execute.apply()
    }
  }

  def getAllWords(): List[Word] = {
    DB readOnly { implicit session =>
      sql"""
        select * from Word
      """.map(rs => Word(rs.string("word"))).list.apply()
    }
  }

}



